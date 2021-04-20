package Opt;

import IR.BasicBlock;
import IR.Function;
import IR.Instruction.Br;
import IR.Instruction.Phi;
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

    public boolean deleteBBWithoutPredecessor(Function function) {
        if (function.getBlockSet().isEmpty()) {
            return false;
        }
        boolean changed = false;
        Set<BasicBlock> list = new LinkedHashSet<>(function.getBlockSet());
        for (BasicBlock cur: list) {
            if (!function.getBlockSet().contains(cur)) {
                continue;
            }
            if (cur.getPredecessor().isEmpty() && (cur != function.getEntranceBB())) {
                cur.delete();
                changed = true;
                function.computeDFSListAgain = true;
                function.computePostDFSListAgain = true;
                function.computePostReverseDFSListAgain = true;
            } else if (cur.getHeadInst() instanceof Br) {
//                 deleteBBHasOnlyOneInst----Br
                if (((Br) cur.getHeadInst()).getCond() == null) {
                    BasicBlock target = ((Br) cur.getHeadInst()).getThenBlock();
                    if (!(target.getHeadInst() instanceof Phi)){
                        Set<BasicBlock> tmp = new LinkedHashSet<>(cur.getPredecessor());
                        for (BasicBlock pre : tmp) {
                            pre.getTailInst().replaceBBUse(cur, target);
                        }
                        cur.delete();
                        changed = true;
                        function.computeDFSListAgain = true;
                        function.computePostDFSListAgain = true;
                        function.computePostReverseDFSListAgain = true;
                    }
                }
            } else if ((cur.getSuccessor().size() == 1) && (cur.getSuccessor().iterator().next().getPredecessor().size() <= 1)) {
                BasicBlock tmp = cur.getSuccessor().iterator().next();
                cur.mergeWithSuccessor(tmp);
                changed = true;
                function.computeDFSListAgain = true;
                function.computePostDFSListAgain = true;
                function.computePostReverseDFSListAgain = true;
            }
        }
        return changed;
    }
}
