package Opt;

import IR.BasicBlock;
import IR.Function;
import IR.Instruction.Br;
import IR.Module;

import java.util.LinkedHashSet;
import java.util.Set;

public class CFGSimplifier {
    private final Module module;

    public CFGSimplifier(Module module) {
        this.module = module;
    }

    public void run() {
        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal()) {
                deleteBBWithoutPredecessor(function);
            }
        }
    }

    public void deleteBBWithoutPredecessor(Function function) {
        for (BasicBlock cur = function.getHeadBB().getNextBB(); cur != null; cur = cur.getNextBB()) {
            if (cur.getPredecessor().isEmpty()) {
                cur.delete();
            } else if (cur.getHeadInst() instanceof Br) {
//                 deleteBBHasOnlyOneInst----Br
                BasicBlock target = ((Br) cur.getHeadInst()).getThenBlock();
                Set<BasicBlock> tmp = new LinkedHashSet<>(cur.getPredecessor());
                for (BasicBlock pre: tmp) {
                    pre.getTailInst().replaceBBUse(cur, target);
                }
                cur.delete();
            } else if ((cur.getSuccessor().size() == 1) && (cur.getSuccessor().iterator().next().getPredecessor().size() == 1)) {
                BasicBlock tmp = cur.getSuccessor().iterator().next();
                cur.mergeWithSuccessor(tmp);
            }
        }
    }
}
