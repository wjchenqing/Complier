package Opt;

import IR.BasicBlock;
import IR.Function;
import IR.Instruction.Br;
import IR.Instruction.IRInst;
import IR.Instruction.IRMove;
import IR.Instruction.Phi;
import IR.Module;
import IR.Operand.IROper;
import Util.Pair;

import java.util.*;

public class SSADestructor {
    private Module module;

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
        BasicBlock newBB_head = null;
        BasicBlock newBB_tail = null;
        for (BasicBlock cur = function.getHeadBB().getNextBB(); cur != null; cur = cur.getNextBB()) {
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
                    BasicBlock newBB = new BasicBlock("between_" + predecessor.getName() + "_" + cur.getName(), function);
                    function.CheckAndSetName(newBB.getName(), newBB);
                    if (newBB_head == null) {
                        newBB_head = newBB;
                        newBB_tail = newBB;
                    } else {
                        newBB_tail.setNextBB(newBB);
                        newBB.setPrevBB(newBB_tail);
                        newBB_tail = newBB;
                    }

                    predecessor.getTailInst().replaceBBUse(cur, newBB);
                    newBB.addInstAtTail(new Br(newBB, null, cur, null));

                    insertLocations.put(predecessor, newBB.getTailInst());
                } else {
                    insertLocations.put(predecessor, predecessor.getTailInst());
                }
            }

            for (IRInst phi: phiList) {
                for (Pair<BasicBlock, IROper> pair: ((Phi) phi).getPossiblePredecessorSet()) {
                    IRInst location = insertLocations.get(pair.getFirst());
                    location.addInstPrev(new IRMove(location.getCurrentBB(), phi.getResult(), pair.getSecond()));
                }
                phi.deleteInst();
            }
        }

        if (function.getReturnBB() != null) {
            BasicBlock cur = function.getReturnBB();
            ArrayList<Phi> phiList = new ArrayList<>();
            for (IRInst phi = cur.getHeadInst(); phi instanceof Phi; phi = phi.getNextInst()) {
                phiList.add((Phi) phi);
            }
            if (!phiList.isEmpty()) {
                Map<BasicBlock, IRInst> insertLocations = new LinkedHashMap<>();
                Set<BasicBlock> predecessors = new LinkedHashSet<>(cur.getPredecessor());
                for (BasicBlock predecessor : predecessors) {
                    if (predecessor.getSuccessor().size() > 1) {
                        BasicBlock newBB = new BasicBlock("between_" + predecessor.getName() + "_" + cur.getName(), function);
                        function.CheckAndSetName(newBB.getName(), newBB);
                        if (newBB_head == null) {
                            newBB_head = newBB;
                            newBB_tail = newBB;
                        } else {
                            newBB_tail.setNextBB(newBB);
                            newBB.setPrevBB(newBB_tail);
                            newBB_tail = newBB;
                        }

                        predecessor.getTailInst().replaceBBUse(cur, newBB);
                        newBB.addInstAtTail(new Br(newBB, null, cur, null));

                        insertLocations.put(predecessor, newBB.getTailInst());
                    } else {
                        insertLocations.put(predecessor, predecessor.getTailInst());
                    }
                }

                for (IRInst phi: phiList) {
                    for (Pair<BasicBlock, IROper> pair : ((Phi) phi).getPossiblePredecessorSet()) {
                        IRInst location = insertLocations.get(pair.getFirst());
                        location.addInstPrev(new IRMove(location.getCurrentBB(), phi.getResult(), pair.getSecond()));
                    }
                    phi.deleteInst();
                }
            }
        }

        if (newBB_head != null){
            function.getTailBB().setNextBB(newBB_head);
            newBB_head.setPrevBB(function.getTailBB());
            function.setTailBB(newBB_tail);
        }
    }
}
