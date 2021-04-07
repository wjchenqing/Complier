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

    public void constructDominatorTree(Function function) {
        function.getHeadBB().dom = function.getHeadBB();
        boolean changed = true;
        ArrayList<BasicBlock> dfsList = function.getDfsList();
        while (changed) {
            changed = false;
            for (int i = dfsList.size() - 1; i > 0; --i) {
                BasicBlock b = dfsList.get(i);
                BasicBlock choose = b.getPredecessor().iterator().next();
                BasicBlock new_iDom = choose;
                for (BasicBlock p: b.getPredecessor()) {
                    if (p == choose) {
                        continue;
                    }
                    if (p.dom != null) {
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
            while (finger1.dfsNum > finger2.dfsNum) {
                finger1 = finger1.dom;
            }
            while (finger2.dfsNum > finger1.dfsNum) {
                finger2 = finger2.dom;
            }
        }
        return finger1;
    }

    public void computeDominanceFrontier(Function function) {
        ArrayList<BasicBlock> dfsList = function.getDfsList();
        for (BasicBlock b: dfsList) {
            if (b.getPredecessor().size() == 1) {
                continue;
            }
            for (BasicBlock p: b.getPredecessor()) {
                BasicBlock runner = p;
                while (runner.dfsNum != b.dom.dfsNum) {
                    runner.DominanceFrontier.add(b);
                    runner = runner.dom;
                }
            }
        }
    }
}
