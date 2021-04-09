package Opt;

import IR.BasicBlock;
import IR.Function;
import IR.Module;
import Util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DominatorTree {
    private Module module;

    public DominatorTree(Module module) {
        this.module = module;
    }

    public void run() {
        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal()) {
                constructDominatorTree(function);
                computeDominanceFrontier(function);
            }
        }
    }

    public void constructDominatorTree(Function function) {
        function.getHeadBB().dom = function.getHeadBB();
        boolean changed = true;
        ArrayList<BasicBlock> postDfsList = function.getPostDfsList();
        while (changed) {
            changed = false;
            for (int i = postDfsList.size() - 2; i >= 0; --i) {
                BasicBlock b = postDfsList.get(i);
                BasicBlock new_iDom = null;
                for (BasicBlock p: b.getPredecessor()) {
                    if (p.postDfsNum == 0) {
                        continue;
                    }
                    if ((new_iDom == null) && (p.dom != null)) {
                        new_iDom = p;
                    }else if (p.dom != null) {
                        new_iDom = intersect(p, new_iDom);
                    }
                }
                if (b.dom != new_iDom) {
                    b.dom = new_iDom;
                    changed = true;
                }
            }
        }
    }

    private BasicBlock intersect(BasicBlock b1, BasicBlock b2) {
        BasicBlock finger1 = b1;
        BasicBlock finger2 = b2;
        while (finger1 != finger2) {
            while (finger1.postDfsNum < finger2.postDfsNum) {
                finger1 = finger1.dom;
            }
            while (finger2.postDfsNum < finger1.postDfsNum) {
                finger2 = finger2.dom;
            }
        }
        return finger1;
    }

    public void computeDominanceFrontier(Function function) {
        ArrayList<BasicBlock> postDfsList = function.getPostDfsList();
        for (BasicBlock b: postDfsList) {
            if (b.getPredecessor().size() == 1) {
                continue;
            }
            for (BasicBlock p: b.getPredecessor()) {
                if (p.postDfsNum == 0) {
                    continue;
                }
                BasicBlock runner = p;
                while (runner != b.dom) {
                    runner.DominanceFrontier.add(b);
                    runner = runner.dom;
                }
            }
        }
    }
}
