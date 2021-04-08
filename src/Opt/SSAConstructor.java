package Opt;

import IR.BasicBlock;
import IR.Function;
import IR.Instruction.IRInst;
import IR.Instruction.Load;
import IR.Instruction.Phi;
import IR.Instruction.Store;
import IR.Module;
import IR.Operand.IROper;
import IR.Operand.Register;
import IR.Type.PointerType;
import Util.Pair;

import java.util.*;

public class SSAConstructor {
    private Module module;
    private Map<BasicBlock, Map<Register, Phi>> Phis;
    private Set<BasicBlock> visited;
    private Map<Register, Stack<Pair<BasicBlock, IROper>>> defStacks;
    private Set<Register> allocaResults;
    private Function function;

    public SSAConstructor(Module module) {
        this.module = module;
    }

    public void run() {
        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal()) {
                InsertingPhi(function);
            }
        }
    }

    public void InsertingPhi(Function function) {
        Phis = new HashMap<>();
        visited = new HashSet<>();
        defStacks = new HashMap<>();
        allocaResults = function.allocaResults;
        this.function = function;
        for (Register allocaResult: function.allocaResults) {
            defStacks.put(allocaResult, new Stack<>());
            Set<BasicBlock> F = new HashSet<>();            //set of bbs where Phi is added
            Set<BasicBlock> W = new HashSet<>();            //set of bbs that contains store to allocaResult
            Set<BasicBlock> Defs = new HashSet<>();
            for (IRInst irInst: allocaResult.getUses()) {
                if (irInst instanceof Store) {
                    W.add(irInst.getCurrentBB());
                    Defs.add(irInst.getCurrentBB());
                } else {
                    assert irInst instanceof Load;
                }
            }
            while (!W.isEmpty()) {
                BasicBlock X = W.iterator().next();
                W.remove(X);
                for (BasicBlock Y: X.DominanceFrontier) {
                    if (!F.contains(Y)) {
                        addPhi(Y, allocaResult);
                        F.add(Y);
                        if (!Defs.contains(Y)) {
                            W.add(Y);
                            Defs.add(Y);
                        }
                    }
                }
            }
        }

        rename(function.getHeadBB(), null);

        for (Register register: allocaResults) {
            for (IRInst irInst: register.getDefs()) {
                irInst.deleteInst();
            }
        }
    }

    private void addPhi(BasicBlock Y, Register allocaResult) {
        String name = allocaResult.getName() + "_ssa";
        Register newResult = new Register(((PointerType) allocaResult.getType()).getType(), name);
        function.CheckAndSetName(newResult.getName(), newResult);
        Phi newPhi = new Phi(Y, newResult, new HashSet<>());
        putIntoPhis(Y, allocaResult, newPhi);
    }

    private void putIntoPhis(BasicBlock bb, Register allocaResult, Phi phi) {
        if (Phis.containsKey(bb)) {
            Phis.get(bb).put(allocaResult, phi);
        } else {
            Map<Register, Phi> tmp = new HashMap<>();
            tmp.put(allocaResult, phi);
            Phis.put(bb, tmp);
        }
    }

    public void rename(BasicBlock basicBlock, BasicBlock predecessor) {
        Map<Register, Phi> newPhis = Phis.get(basicBlock);
        if (newPhis != null){
            for (Register allocaResult : newPhis.keySet()) {
                Phi phi = newPhis.get(allocaResult);
                Stack<Pair<BasicBlock, IROper>> defStack = defStacks.get(allocaResult);
                phi.addPair(predecessor, defStack.peek().getSecond());
                if (!visited.contains(basicBlock)) {
                    defStack.add(new Pair<>(basicBlock, phi.getResult()));
                }
            }
        }
        if (visited.contains(basicBlock)) {
            return;
        }
        visited.add(basicBlock);

        for (IRInst irInst = basicBlock.getHeadInst(); irInst != null; irInst = irInst.getNextInst()) {
            if (irInst instanceof Load) {
                IROper pointer = ((Load) irInst).getPointer();
                if ((pointer instanceof Register) && (allocaResults.contains(pointer))) {
                    irInst.getResult().replaceUse(defStacks.get(pointer).peek().getSecond());
                    irInst.deleteInst();
                }
            } else if (irInst instanceof  Store) {
                IROper pointer = ((Store) irInst).getPointer();
                if ((pointer instanceof Register) && (allocaResults.contains(pointer))) {
                    defStacks.get(pointer).add(new Pair<>(basicBlock, ((Store) irInst).getValue()));
                    irInst.deleteInst();
                }
            }
        }

        for (BasicBlock successor: basicBlock.getSuccessor()) {
            rename(successor, basicBlock);
        }

        for (Stack<Pair<BasicBlock, IROper>> defStack: defStacks.values()) {
            while (!(defStack.empty()) && (defStack.peek().getFirst() == basicBlock)) {
                defStack.pop();
            }
        }

        if (newPhis != null){
            for (Phi phi : newPhis.values()) {
                basicBlock.addInstAtHead(phi);
            }
        }
    }
}
