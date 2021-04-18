package Opt;

import IR.BasicBlock;
import IR.Function;
import IR.Instruction.*;
import IR.Module;
import IR.Operand.IROper;
import IR.Operand.Register;
import Util.Pair;

import java.util.*;

public class DeadCodeEliminator {
    private Module module;
    private SideEffectChecker sideEffectChecker;

    private boolean changed;

    public DeadCodeEliminator(Module module, SideEffectChecker sideEffectChecker) {
        this.module = module;
        this.sideEffectChecker = sideEffectChecker;
    }

    public boolean run() {
        changed = false;
        sideEffectChecker.run();
        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal()) {
                eliminator(function);
            }
        }
        return changed;
    }

    private void eliminator(Function function) {
        Queue<IRInst> queue = new LinkedList<>();
        Set<IRInst> liveInst = new LinkedHashSet<>();

        for (BasicBlock basicBlock: function.getBlockSet()) {
            IRInst tail = basicBlock.getTailInst();
            boolean containsTail = liveInst.contains(tail);
            for (IRInst irInst = basicBlock.getHeadInst(); irInst != null; irInst = irInst.getNextInst()) {
                if (irInst instanceof Call) {
                    if (((Call) irInst).getFunction().hasSideEffect) {
                        liveInst.add(irInst);
                        queue.offer(irInst);
                        if (!containsTail) {
                            liveInst.add(tail);
                            queue.offer(tail);
                            containsTail = true;
                        }
                    }

                } else if (irInst instanceof Store) {
                    liveInst.add(irInst);
                    queue.offer(irInst);
                    if (!containsTail) {
                        liveInst.add(tail);
                        queue.offer(tail);
                        containsTail = true;
                    }
                } else if (irInst instanceof Ret) {
                    if (!containsTail) {
                        liveInst.add(tail);
                        queue.offer(tail);
                        containsTail = true;
                    }
                }
            }
        }

        while (!queue.isEmpty()) {
            IRInst irInst = queue.poll();

            for (IROper use: irInst.getUses()) {
                if (use instanceof Register) {
                    for (IRInst defInst: use.getDefs()) {
                        if (!liveInst.contains(defInst)) {
                            liveInst.add(defInst);
                            queue.offer(defInst);
                        }
                        IRInst tail = defInst.getCurrentBB().getTailInst();
                        if (!(tail instanceof Ret) && !(liveInst.contains(tail))) {
                            liveInst.add(tail);
                            queue.offer(tail);
                        }
                    }
                }
            }
            if (irInst instanceof Phi) {
                for (Pair<BasicBlock, IROper> pair: ((Phi) irInst).getPossiblePredecessorSet()) {
                    IRInst tail = pair.getFirst().getTailInst();
                    if (!(tail instanceof Ret) && !(liveInst.contains(tail))) {
                        liveInst.add(tail);
                        queue.offer(tail);
                    }
                }
            }

            for (BasicBlock basicBlock: irInst.getCurrentBB().reverseDominanceFrontier) {
                IRInst tail = basicBlock.getTailInst();
                if (!(liveInst.contains(tail))) {
                    liveInst.add(tail);
                    queue.offer(tail);
                }
            }

        }

        Set<BasicBlock> blockSet = new LinkedHashSet<>(function.getPostDfsList());
        for (BasicBlock basicBlock: blockSet) {
            if (!function.getBlockSet().contains(basicBlock)) {
                continue;
            }
            for (IRInst inst = basicBlock.getHeadInst(); inst != null; inst = inst.getNextInst()) {
                if (!liveInst.contains(inst)) {
                    inst.deleteInst();
                    changed = true;
                }
            }
        }
    }
}
