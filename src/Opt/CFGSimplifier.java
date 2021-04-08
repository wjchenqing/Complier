package Opt;

import IR.BasicBlock;
import IR.Function;
import IR.Module;

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
            }
        }
    }
}
