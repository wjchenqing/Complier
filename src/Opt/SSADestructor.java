package Opt;

import IR.BasicBlock;
import IR.Function;
import IR.Instruction.Br;
import IR.Instruction.IRInst;
import IR.Instruction.IRMove;
import IR.Instruction.Phi;
import IR.Module;
import IR.Operand.IROper;
import IR.Operand.Register;
import Util.Pair;

import java.util.*;

public class SSADestructor {
    private Module module;
    private Map<IRInst, Set<IRMove>> Moves;

    public SSADestructor(Module module) {
        this.module = module;
    }

    public void run() {
        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal()) {
                CriticalEdgeSplitting(function);
            }
        }
    }

    public void CriticalEdgeSplitting(Function function) {
        Set<BasicBlock> newBBList = new LinkedHashSet<>();
        Moves = new LinkedHashMap<>();
        for (BasicBlock cur: function.getBlockSet()) {
            ArrayList<Phi> phiList = new ArrayList<>();
            for (IRInst phi = cur.getHeadInst(); phi instanceof Phi; phi = phi.getNextInst()) {
                phiList.add((Phi) phi);
            }
            if (phiList.isEmpty()) {
                continue;
            }
            Map<BasicBlock, IRInst> insertLocations = new LinkedHashMap<>();
            Set<BasicBlock> predecessors = new LinkedHashSet<>(cur.getPredecessor());
            for (BasicBlock predecessor: predecessors) {
                if (predecessor.getSuccessor().size() > 1) {
                    BasicBlock newBB = new BasicBlock("between_" + predecessor.getName() + "_" + cur.getName(), function, predecessor.depth);
                    function.CheckAndSetName(newBB.getName(), newBB);
                    newBBList.add(newBB);
                    function.computePostDFSListAgain = true;
                    function.computeDFSListAgain = true;
                    function.computePostReverseDFSListAgain = true;

                    predecessor.getTailInst().replaceBBUse(cur, newBB);
                    newBB.addInstAtTail(new Br(newBB, null, cur, null));

                    insertLocations.put(predecessor, newBB.getTailInst());
                } else {
                    insertLocations.put(predecessor, predecessor.getTailInst());
                }
            }

            for (IRInst phi: phiList) {
                Set<Pair<BasicBlock, IROper>> possibleValSet = new LinkedHashSet<>(((Phi) phi).getPossiblePredecessorSet());
                boolean sameAns = true;
                IROper ans = null;
                for (Pair<BasicBlock, IROper> pair: possibleValSet) {
//                    IRInst location = insertLocations.get(pair.getFirst());
//                    if (location == null) {
//                        continue;
//                    }
//                    possibleValSet.add(pair);
                    if (sameAns) {
                        if (ans == null) {
                            ans = pair.getSecond();
                        } else if (ans != pair.getSecond()) {
                            sameAns = false;
                        }
                    }
                }
                if (sameAns) {
                    phi.addInstPrev(new IRMove(phi.getCurrentBB(), phi.getResult(), ans));
                    phi.deleteInst();
                    continue;
                }
                for (Pair<BasicBlock, IROper> pair: possibleValSet) {
                    if (phi.getResult() == pair.getSecond()) {
                        continue;
                    }
                    IRInst location = insertLocations.get(pair.getFirst());
                    assert location != null;
                    if (!Moves.containsKey(location)) {
                        Set<IRMove> tmp = new LinkedHashSet<>();
                        tmp.add(new IRMove(location.getCurrentBB(), phi.getResult(), pair.getSecond()));
                        Moves.put(location, tmp);
                    } else {
                        Moves.get(location).add(new IRMove(location.getCurrentBB(), phi.getResult(), pair.getSecond()));
                    }
//                    location.addInstPrev(new IRMove(location.getCurrentBB(), phi.getResult(), pair.getSecond()));
                }
                phi.deleteInst();
            }
        }

        for (IRInst location: Moves.keySet()) {
            Set<IRMove> moveSet = Moves.get(location);
            ArrayList<IRMove> seq = new ArrayList<>();
            while (!moveSet.isEmpty()) {
                boolean changed = true;
                while (changed) {
                    changed = false;
                    Set<IRMove> list = new LinkedHashSet<>(moveSet);
                    for (IRMove test : list) {
                        boolean canAdd = true;
                        for (IRMove tmp : moveSet) {
                            if (test == tmp) {
                                continue;
                            }
                            if (test.getResult() == tmp.getSource()) {
                                canAdd = false;
                                break;
                            }
                        }
                        if (canAdd) {
                            changed = true;
                            seq.add(test);
                            moveSet.remove(test);
                        }
                    }
                }
                if (!moveSet.isEmpty()) {
                    IRMove move = moveSet.iterator().next();
                    IROper a = move.getSource();
                    Register newReg = new Register(a.getType(), a.getName());
                    function.CheckAndSetName(newReg.getName(), newReg);
                    IRMove newMove = new IRMove(location.getCurrentBB(), newReg, a);
                    move.replaceUse(a, newReg);
                    seq.add(newMove);
                }
            }
            for (IRMove move: seq) {
                location.addInstPrev(move);
            }
        }

        function.getBlockSet().addAll(newBBList);
    }
}
