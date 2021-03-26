package Codegen.Backend;

import Codegen.BasicBlock;
import Codegen.Function;
import Codegen.Instruction.*;
import Codegen.Module;
import Codegen.Operand.Addr;
import Codegen.Operand.RegisterPhysical;
import Codegen.Operand.RegisterVirtual;
import Util.Edge;

import java.util.*;

public class RegisterAllocator {
    private final int K = 28;

    private Module module;
    private Function function;

    private final Set<RegisterVirtual> precolored = new HashSet<>();
    private final Set<RegisterVirtual> initial = new HashSet<>();
    private final Set<RegisterVirtual> simplifyWorkList = new HashSet<>();
    private final Set<RegisterVirtual> freezeWorkList = new HashSet<>();
    private final Set<RegisterVirtual> spillWorkList = new HashSet<>();
    private final Set<RegisterVirtual> spilledNodes = new HashSet<>();
    private final Set<RegisterVirtual> coalescedNodes = new HashSet<>();
    private final Set<RegisterVirtual> coloredNodes = new HashSet<>();

    private final Stack<RegisterVirtual> selectStack = new Stack<>();

    private final Set<Move> coalescedMoves = new HashSet<>();
    private final Set<Move> constrainedMoves = new HashSet<>();
    private final Set<Move> frozenMoves = new HashSet<>();
    private final Set<Move> workListMoves = new HashSet<>();
    private final Set<Move> activeMoves = new HashSet<>();

    private final Set<Edge> adjSet = new HashSet<>();

    public RegisterAllocator(Module module) {
        this.module = module;
    }

    public void runAll() {
        for (Function function: module.getFunctionMap().values()) {
            if (function.getIrFunction().isNotExternal()) {
                this.function = function;
                run();
            }
        }
    }

    public void init() {
        initial.addAll(function.getOperandMap().values());
        precolored.addAll(RegisterPhysical.virtualMap.values());
        initial.removeAll(precolored);
    }

    public void run() {
        init();
        new LiveAnalysis(module);
        build();
        makeWorkList();
        while (!(simplifyWorkList.isEmpty() && workListMoves.isEmpty() && freezeWorkList.isEmpty() && spillWorkList.isEmpty())) {
            if (!simplifyWorkList.isEmpty()) {
                simplify();
            } else if (!workListMoves.isEmpty()) {
                coalesce();
            } else if (!freezeWorkList.isEmpty()) {
                freeze();
            } else {
                selectSpill();
            }
        }
        assignColors();
        if (!spilledNodes.isEmpty()) {
            rewriteProgram();
            run();
        }
    }

    public void build() {
        for (BasicBlock basicBlock: function.getDfsList()) {
            Set<RegisterVirtual> liveOut = basicBlock.getLiveOut();
            Instruction inst = basicBlock.getTailInst();
            while (inst != null) {
                if (inst instanceof Move) {
                    liveOut.removeAll(inst.getUse());
                    for (RegisterVirtual rv: inst.getUse()) {
                        rv.addToMoveList((Move) inst);
                    }
                    for (RegisterVirtual rv: inst.getDef()) {
                        rv.addToMoveList((Move) inst);
                    }
                }
                liveOut.add(RegisterPhysical.getVR(0));
                liveOut.addAll(inst.getDef());
                for (RegisterVirtual rv: inst.getDef()) {
                    for (RegisterVirtual live: liveOut) {
                        adjSet.add(new Edge(rv, live));
                    }
                }
                liveOut.removeAll(inst.getDef());
                liveOut.addAll(inst.getUse());
                inst = inst.getPrev();
            }
        }
    }

    public void addEdge(RegisterVirtual u, RegisterVirtual v) {
        if ((u != v) && (!adjSet.contains(new Edge(u, v)))) {
            adjSet.add(new Edge(u, v));
            if (!precolored.contains(u)) {
                u.addToAdjList(v);
                u.setDegree(u.getDegree() + 1);
            }
            if (!precolored.contains(v)) {
                v.addToAdjList(u);
                v.setDegree(v.getDegree() + 1);
            }
        }
    }

    public void makeWorkList() {
        for (RegisterVirtual rv: initial) {
            initial.remove(rv);
            if (rv.getDegree() >= K) {
                spillWorkList.add(rv);
            } else if (moveRelated(rv)) {
                freezeWorkList.add(rv);
            } else {
                simplifyWorkList.add(rv);
            }
        }
    }

    public Set<RegisterVirtual> adjacent(RegisterVirtual n) {
        Set<RegisterVirtual> union = new HashSet<>(selectStack);
        union.addAll(coalescedNodes);
        Set<RegisterVirtual> ans = new HashSet<>(n.getAdjList());
        ans.removeAll(union);
        return ans;
    }

    public Set<Move> nodeMoves(RegisterVirtual n) {
        Set<Move> union = new HashSet<>(activeMoves);
        union.addAll(workListMoves);
        Set<Move> tmp = new HashSet<>(n.getMoveList());
        tmp.removeAll(union);
        Set<Move> ans = new HashSet<>(n.getMoveList());
        ans.removeAll(tmp);
        return ans;
    }

    public boolean moveRelated(RegisterVirtual n) {
        return !nodeMoves(n).isEmpty();
    }

    public void simplify() {
        RegisterVirtual n = simplifyWorkList.iterator().next();
        simplifyWorkList.remove(n);
        selectStack.push(n);
        for (RegisterVirtual m: adjacent(n)) {
            decrementDegree(m);
        }
    }

    public void decrementDegree(RegisterVirtual m) {
        int d = m.getDegree();
        m.setDegree(d - 1);
        if (d == K) {
            Set<RegisterVirtual> union = new HashSet<>(adjacent(m));
            union.add(m);
            enableMoves(union);
            spillWorkList.remove(m);
            if (moveRelated(m)) {
                freezeWorkList.add(m);
            } else {
                simplifyWorkList.add(m);
            }
        }
    }

    public void enableMoves(Set<RegisterVirtual> nodes) {
        for (RegisterVirtual n: nodes) {
            for (Move m: nodeMoves(n)) {
                if (activeMoves.contains(m)) {
                    activeMoves.remove(m);
                    workListMoves.add(m);
                }
            }
        }
    }

    public void addWorkList(RegisterVirtual u) {
        if ((!precolored.contains(u)) && (!moveRelated(u)) && (u.getDegree() < K)) {
            freezeWorkList.remove(u);
            simplifyWorkList.add(u);
        }
    }

    public boolean ok(RegisterVirtual t, RegisterVirtual r) {
        return ((t.getDegree() < K) || precolored.contains(t) || adjSet.contains(new Edge(t, r)));
    }

    public boolean conservative(Set<RegisterVirtual> nodes) {
        int k = 0;
        for (RegisterVirtual n: nodes) {
            if (n.getDegree() > K) {
                k++;
            }
        }
        return k < K;
    }

    public void coalesce() {
        Move m = workListMoves.iterator().next();
        RegisterVirtual x = getAlias((RegisterVirtual) m.getRd());
        RegisterVirtual y = getAlias((RegisterVirtual) m.getRs());
        RegisterVirtual u;
        RegisterVirtual v;
        if (precolored.contains(y)) {
            u = y;
            v = x;
        } else {
            u = x;
            v = y;
        }
        workListMoves.remove(m);
        if (u == v) {
            coalescedMoves.add(m);
            addWorkList(u);
        } else if ((precolored.contains(v)) || (adjSet.contains(new Edge(u, v)))) {
            constrainedMoves.add(m);
            addWorkList(u);
            addWorkList(v);
        } else if (((precolored.contains(u)) && (testOK(adjacent(v), u)))
                || ((!precolored.contains(u)) && (conservative(unionAdjacentResult(u, v))))) {
            coalescedMoves.add(m);
            combine(u, v);
            addWorkList(u);
        } else {
            activeMoves.add(m);
        }
    }

    private boolean testOK(Set<RegisterVirtual> nodes, RegisterVirtual u) {
        for (RegisterVirtual t: nodes) {
            if (!ok(t, u)) {
                return false;
            }
        }
        return true;
    }

    private Set<RegisterVirtual> unionAdjacentResult(RegisterVirtual u, RegisterVirtual v) {
        Set<RegisterVirtual> ans = new HashSet<>(adjacent(u));
        ans.addAll(adjacent(v));
        return ans;
    }

    public void combine(RegisterVirtual u, RegisterVirtual v) {
        if (freezeWorkList.contains(v)) {
            freezeWorkList.remove(v);
        } else {
            spillWorkList.remove(v);
        }
        coalescedNodes.add(u);
        v.setAlias(u);
        u.getMoveList().addAll(v.getMoveList());
        Set<RegisterVirtual> tmp = new HashSet<>();
        tmp.add(v);
        enableMoves(tmp);
        for (RegisterVirtual t: adjacent(v)) {
            addEdge(t, u);
            decrementDegree(t);
        }
        if ((u.getDegree() >= K) && (freezeWorkList.contains(u))) {
            freezeWorkList.remove(u);
            spillWorkList.add(u);
        }
    }

    public RegisterVirtual getAlias(RegisterVirtual n) {
        if (coalescedNodes.contains(n)) {
            return getAlias(n.getAlias());
        }
        else return n;
    }

    public void freeze() {
        RegisterVirtual u = freezeWorkList.iterator().next();
        freezeWorkList.remove(u);
        simplifyWorkList.add(u);
        freezeMoves(u);
    }

    public void freezeMoves(RegisterVirtual u) {
        for (Move m: nodeMoves(u)) {
            RegisterVirtual x = (RegisterVirtual) m.getRd();
            RegisterVirtual y = (RegisterVirtual) m.getRs();
            RegisterVirtual v;
            if (getAlias(y) == getAlias(u)) {
                v = getAlias(x);
            } else {
                v = getAlias(y);
            }
            activeMoves.remove(m);
            frozenMoves.add(m);
            if (freezeWorkList.contains(v) && (nodeMoves(v).isEmpty())) {
                freezeWorkList.remove(v);
                simplifyWorkList.add(v);
            }
        }
    }


    public void selectSpill() {
        RegisterVirtual m = spillWorkList.iterator().next(); // todo: select using favorite heuristic.
        spillWorkList.remove(m);
        simplifyWorkList.add(m);
        freezeMoves(m);
    }

    public void assignColors() {
        while (!selectStack.isEmpty()) {
            RegisterVirtual n = selectStack.pop();
            Set<RegisterPhysical> okColors = new HashSet<>(RegisterPhysical.colorSet);
            for (RegisterVirtual w: n.getAdjList()) {
                Set<RegisterVirtual> union = new HashSet<>(coloredNodes);
                union.addAll(precolored);
                if (union.contains(getAlias(w))) {
                    okColors.remove(getAlias(w).getColor());
                }
            }
            if (okColors.isEmpty()) {
                spilledNodes.add(n);
            } else {
                coloredNodes.add(n);
                RegisterPhysical c = okColors.iterator().next();
                n.setColor(c);
            }
        }
        for (RegisterVirtual n: coalescedNodes) {
            n.setColor(getAlias(n).getColor());
        }
    }

    private final Map<RegisterVirtual, Addr> spillAddrMap = new HashMap<>();

    public void rewriteProgram() {
        for (RegisterVirtual rv: spilledNodes) {
            Addr addr = new Addr(true, rv, null);
            function.getStack().putSpillLocation(rv, addr);
            spillAddrMap.put(rv, addr);
        }
        for (Instruction inst: function.getInstList()) {
            for (RegisterVirtual rv: inst.getDef()) {
                Addr addr = spillAddrMap.get(rv);
                if (addr != null) {
                    RegisterVirtual n = new RegisterVirtual(rv.getName());
                    function.CheckAndSetName(n.getName(), n);
                    inst.replaceDef(rv, n);
                    inst.addInstNext(new Store(inst.getBasicBlock(), Store.Name.sw, n, addr));
                }
            }
            for (RegisterVirtual rv: inst.getUse()) {
                Addr addr = spillAddrMap.get(rv);
                if (addr != null) {
                    RegisterVirtual n = new RegisterVirtual(rv.getName());
                    function.CheckAndSetName(n.getName(), n);
                    inst.replaceUse(rv, n);
                    inst.addInstPrev(new LoadGlobal(inst.getBasicBlock(), LoadGlobal.Name.lw, n, addr));
                }
            }
        }
        spilledNodes.clear();
        spillAddrMap.clear();
    }
}
