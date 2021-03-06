package Codegen.Backend;

import Codegen.BasicBlock;
import Codegen.Function;
import Codegen.Instruction.*;
import Codegen.Module;
import Codegen.Operand.Addr;
import Codegen.Operand.ImmediateInt;
import Codegen.Operand.RegisterPhysical;
import Codegen.Operand.RegisterVirtual;
import Util.Edge;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RegisterAllocator {
    private final int K = 28;

    private Module module;
    private Function function;

    private Set<RegisterVirtual> precolored;
    private Set<RegisterVirtual> initial;
    private Set<RegisterVirtual> simplifyWorkList;
    private Set<RegisterVirtual> freezeWorkList;
    private Set<RegisterVirtual> spillWorkList;
    private Set<RegisterVirtual> spilledNodes;
    private Set<RegisterVirtual> coalescedNodes;
    private Set<RegisterVirtual> coloredNodes;

    private Stack<RegisterVirtual> selectStack;

//    private Set<Move> coalescedMoves;
//    private Set<Move> constrainedMoves;
//    private Set<Move> frozenMoves;
    private Set<Move> workListMoves;
    private Set<Move> activeMoves;

    private Set<Edge> adjSet;

    private Map<RegisterVirtual, Set<Instruction>> regUseIn;
    private Map<RegisterVirtual, Set<Instruction>> regDefIn;

    public RegisterAllocator(Module module) {
        this.module = module;
    }

    public void runAll() {
        for (Function function: module.getFunctionMap().values()) {
            if (function.getIrFunction().isNotExternal()) {
//                System.out.println("Start allocating for " + function.getName());
                this.function = function;
                run();
                function.getStack().setAndGetSize();
                setSP();
                for (Instruction instruction: function.getInstList()) {
                    if (instruction instanceof Move) {
                        RegisterPhysical rdColor, rsColor;
                        rdColor = (((Move) instruction).getRd() instanceof RegisterPhysical) ? (RegisterPhysical) ((Move) instruction).getRd() : ((RegisterVirtual) ((Move) instruction).getRd()).getColor();
                        rsColor = (((Move) instruction).getRs() instanceof RegisterPhysical) ? (RegisterPhysical) ((Move) instruction).getRs() : ((RegisterVirtual) ((Move) instruction).getRs()).getColor();
                        if (rdColor == rsColor) {
                            instruction.deleteInst();
                        }
                    }
                }
//                System.out.println();
            }
        }
    }

    public void init() {
        precolored = new LinkedHashSet<>();
        initial = new LinkedHashSet<>();
        simplifyWorkList = new LinkedHashSet<>();
        freezeWorkList = new LinkedHashSet<>();
        spillWorkList = new LinkedHashSet<>();
        spilledNodes = new LinkedHashSet<>();
        coalescedNodes = new LinkedHashSet<>();
        coloredNodes = new LinkedHashSet<>();
        selectStack = new Stack<>();
//        coalescedMoves = new HashSet<>();
//        constrainedMoves = new HashSet<>();
//        frozenMoves = new HashSet<>();
        workListMoves = new LinkedHashSet<>();
        activeMoves = new LinkedHashSet<>();
        adjSet = new LinkedHashSet<>();
        regDefIn = new LinkedHashMap<>();
        regUseIn = new LinkedHashMap<>();

        initial.addAll(function.getOperandMap().values());
        precolored.addAll(RegisterPhysical.virtualMap.values());
        initial.removeAll(precolored);

        for (RegisterVirtual rv: initial) {
            rv.setColor(null);
            rv.setDegree(0);
            rv.setAlias(null);
            rv.setAdjList(new HashSet<>());
            rv.setMoveList(new HashSet<>());
        }

        for (RegisterVirtual rv: precolored) {
            rv.setDegree(0);
            rv.setAlias(null);
            rv.setAdjList(new HashSet<>());
            rv.setMoveList(new HashSet<>());
        }
    }


    public void run() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        int cnt = 1;
        while (true) {
            System.out.println("run round: " + cnt);
            ++cnt;
            init();
            computeSpillCost();
            LiveAnalysis.analysis(function);
            System.out.println("Start building: " + dtf.format(LocalDateTime.now()));
            build();
            System.out.println("Finish building: " + dtf.format(LocalDateTime.now()));

            makeWorkList();
            System.out.println("Finish making worklist: " + dtf.format(LocalDateTime.now()));
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
            System.out.println("Finish while loop: " + dtf.format(LocalDateTime.now()));
            assignColors();
            System.out.println("Finish assigning colors: " + dtf.format(LocalDateTime.now()));
            if (spilledNodes.isEmpty()) {
                break;
            }
            rewriteProgram();
            System.out.println("Finish rewriting: " + dtf.format(LocalDateTime.now()));

//            try {
//                CodegenPrinter codegenPrinter_before = new CodegenPrinter("judger/before_1.s");
//                module.accept(codegenPrinter_before);
//                codegenPrinter_before.getPrintWriter().close();
//                codegenPrinter_before.getOutputStream().close();
//            } catch (IOException e) {
////                 do nothing
//            }

        }
    }

    public void setSP() {
        if (function.getStack().getSize() == 0) {
            return;
        }
        RegisterVirtual sp = RegisterPhysical.getVR(2);
        function.getHeadBB().addInstAtFront(new BinaryInstruction(function.getHeadBB(), BinaryInstruction.Name.addi, true,
                sp, sp, new ImmediateInt(- 4 * function.getStack().getSize())));
        for (BasicBlock basicBlock: function.getBlockList()) {
            if (basicBlock.getTailInst() instanceof Return) {
                basicBlock.getTailInst().addInstPrev(new BinaryInstruction(basicBlock, BinaryInstruction.Name.addi, true,
                        sp, sp, new ImmediateInt(4 * function.getStack().getSize())));
            }
        }
    }


    private void computeSpillCost() {
        for (BasicBlock basicBlock: function.getDfsList()) {
            int depth = basicBlock.depth;
            Instruction ptr = basicBlock.getHeadInst();
            while (ptr != null) {
                for (RegisterVirtual def: ptr.getDef()) {
                    def.spillCost += Math.pow(10, depth);
                }
                for (RegisterVirtual use: ptr.getUse()) {
                    if (use == null) {
                        assert false;
                    }
                    use.spillCost += Math.pow(10, depth);
                }
                ptr = ptr.getNext();
            }
        }
    }

    public void build() {
        regDefIn.clear();
        regUseIn.clear();
        for (BasicBlock basicBlock: function.getDfsList()) {
            Set<RegisterVirtual> liveOut = basicBlock.getLiveOut();
            Instruction inst = basicBlock.getTailInst();
//            ArrayList<Instruction> instructions = basicBlock.getInstList();
//            System.out.println("instructions.size() = " + instructions.size() + "; instNum = " + basicBlock.getInstNum());
            for (int i = basicBlock.instNum - 1; i >= 0; --i) {
//                Instruction inst = instructions.get(i);
                for (RegisterVirtual rv: inst.getUse()) {
                    if (!regUseIn.containsKey(rv)) {
                        Set<Instruction> tmp = new HashSet<>();
                        tmp.add(inst);
                        regUseIn.put(rv, tmp);
                    } else {
                        regUseIn.get(rv).add(inst);
                    }
                }
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
                    if (!regDefIn.containsKey(rv)) {
                        Set<Instruction> tmp = new HashSet<>();
                        tmp.add(inst);
                        regDefIn.put(rv, tmp);
                    } else {
                        regDefIn.get(rv).add(inst);
                    }
                    for (RegisterVirtual live: liveOut) {
                        addEdge(rv, live);
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
            if (rv.getDegree() >= K) {
                spillWorkList.add(rv);
            } else if (moveRelated(rv)) {
                freezeWorkList.add(rv);
            } else {
                simplifyWorkList.add(rv);
            }
        }
        initial.clear();
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
        tmp.containsAll(union);
        return tmp;
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
            Set<RegisterVirtual> union = adjacent(m);
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

    public boolean conservative(RegisterVirtual u, RegisterVirtual v) {
        int k = 0;
        Set<RegisterVirtual> visited = new HashSet<>();
        for (RegisterVirtual n: adjacent(u)) {
            visited.add(n);
            if (n.getDegree() >= K) {
                k++;
            }
        }
        for (RegisterVirtual n: adjacent(v)) {
            if (!visited.contains(n)) {
                visited.add(n);
                if (n.getDegree() >= K) {
                    k++;
                }
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
//            coalescedMoves.add(m);
            addWorkList(u);
        } else if ((precolored.contains(v)) || (adjSet.contains(new Edge(u, v)))) {
//            constrainedMoves.add(m);
            addWorkList(u);
            addWorkList(v);
        } else if (((precolored.contains(u)) && (testOK(adjacent(v), u)))
                || ((!precolored.contains(u)) && (conservative(u, v)))) {
//            coalescedMoves.add(m);
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

    public void combine(RegisterVirtual u, RegisterVirtual v) {
        if (freezeWorkList.contains(v)) {
            freezeWorkList.remove(v);
        } else {
            spillWorkList.remove(v);
        }
        coalescedNodes.add(v);
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
//            frozenMoves.add(m);
            if (freezeWorkList.contains(v) && (nodeMoves(v).isEmpty())) {
                freezeWorkList.remove(v);
                simplifyWorkList.add(v);
            }
        }
    }


    public void selectSpill() {
//        RegisterVirtual m = spillWorkList.iterator().next();
        RegisterVirtual m = select();
//        System.out.println("spill " + m.toString() + ", cost = " + computeCost(m) + ", spillCost = " + m.spillCost + ", adjSize = " + m.getAdjList().size());
        spillWorkList.remove(m);
        simplifyWorkList.add(m);
        freezeMoves(m);
    }

    private double computeCost(RegisterVirtual rv) {
        boolean hasNegCost = false;
        boolean hasInfCost = false;
        Set<Instruction> def = regDefIn.get(rv);
        Set<Instruction> use = regUseIn.get(rv);
        if (def != null && use != null) {
            Instruction d = def.iterator().next();
            Instruction u = use.iterator().next();
            if (def.size() == 1 && use.size() == 1) {
                if (d instanceof LoadGlobal && u instanceof Store) {
                    hasNegCost = ((LoadGlobal) d).getAddr() == ((Store) u).getAddr();
                }
            }

            hasInfCost = def.size() == 1 && use.size() == 1 && d.equals(u);
        }
        if (hasNegCost) {
            return Double.NEGATIVE_INFINITY;
        } else if (hasInfCost) {
            return Double.POSITIVE_INFINITY;
        } else {
            return rv.spillCost / rv.getDegree();
        }
    }

    private RegisterVirtual select() {
        double min = Double.POSITIVE_INFINITY;
        RegisterVirtual selected = null;
        int minAdjSize = 10000000;
        for (RegisterVirtual rv: spillWorkList) {
            double cur = computeCost(rv);
            if (cur <= min) {
                min = cur;
                selected = rv;
            }
            if (rv.getAdjList().size() < minAdjSize) {
                minAdjSize = rv.getAdjList().size();
            }
        }
        if (minAdjSize < 60) {
            return selected;
        } else {
            return spillWorkList.iterator().next();
        }
    }

    public void assignColors() {
//        System.out.println("selectStack.size() = " + selectStack.size());
        while (!selectStack.isEmpty()) {
            RegisterVirtual n = selectStack.pop();
            Set<RegisterPhysical> okColors = new LinkedHashSet<>(RegisterPhysical.colorSet);
            for (RegisterVirtual w: n.getAdjList()) {
                RegisterVirtual alias = getAlias(w);
                if (coloredNodes.contains(alias) || precolored.contains(alias)) {
                    okColors.remove(getAlias(w).getColor());
                }
                if (okColors.isEmpty()) {
                    break;
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

    public void rewriteProgram() {
//        Map<RegisterVirtual, Addr> spillAddrMap = new HashMap<>();
        for (RegisterVirtual rv: spilledNodes) {
            Addr addr = new Addr(true, new RegisterVirtual(rv.getName() + "_addr"), null);
            function.getStack().putSpillLocation(rv, addr);
//            spillAddrMap.put(rv, addr);
            if (regDefIn.get(rv) != null) {
                for (Instruction inst : regDefIn.get(rv)) {
                    RegisterVirtual n = new RegisterVirtual(rv.getName());
                    function.CheckAndSetName(n.getName(), n);
                    inst.replaceDef(rv, n);
                    inst.addInstNext(new Store(inst.getBasicBlock(), Store.Name.sw, n, addr));
                }
            }
            if (regUseIn.get(rv) != null) {
                for (Instruction inst : regUseIn.get(rv)) {
                    RegisterVirtual n = new RegisterVirtual(rv.getName());
                    function.CheckAndSetName(n.getName(), n);
                    inst.replaceUse(rv, n);
                    inst.addInstPrev(new LoadGlobal(inst.getBasicBlock(), LoadGlobal.Name.lw, n, addr));
                }
            }
        }
        for (RegisterVirtual rv: spilledNodes) {
            function.getOperandMap().remove(rv.getName());
        }
//        System.out.println("finish a round of spill");
        spilledNodes.clear();
    }
}
