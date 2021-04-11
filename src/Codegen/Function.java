package Codegen;

import Codegen.Instruction.Instruction;
import Codegen.Operand.RegisterVirtual;
import IR.Instruction.IRInst;
import IR.Operand.Parameter;

import java.util.*;

public class Function {
    private final String name;
    private final Module module;
    private final IR.Function irFunction;
    private Stack stack;
    private BasicBlock headBB;
    private BasicBlock tailBB;
    private final Map<String, BasicBlock> blockMap = new HashMap<>();
    private int blockNum = 0;

    private BasicBlock returnBB;
    public boolean hasReturnVal = true;
    private final Map<String, RegisterVirtual> OperandMap = new LinkedHashMap<>();

    private final ArrayList<BasicBlock> dfsList = new ArrayList<>();
    private final Set<BasicBlock> visited = new LinkedHashSet<>();

    public Function(String name, Module module, IR.Function irFunction) {
        this.name = name;
        this.module = module;
        this.irFunction = irFunction;

        if (irFunction.isNotExternal()) {
            for (IR.BasicBlock irBasicBlock : irFunction.getBlockList()) {
                BasicBlock basicBlock = new BasicBlock(this, ".LLB" + module.getFunctionMapSize() + "_" + blockNum, irBasicBlock);
                addBasicBlock(basicBlock);
                blockNum++;
            }
            BasicBlock bb = null;
            if (irFunction.getReturnBB() != null) {
                bb = new BasicBlock(this, ".LLB" + module.getFunctionMapSize() + "_" + blockNum, irFunction.getReturnBB());
                addBasicBlock(bb);
                blockNum++;
                returnBB = bb;
            }

            for (IR.BasicBlock irBasicBlock : irFunction.getBlockList()) {
                BasicBlock basicBlock = getBasicBlock(irBasicBlock.getName());
                for (IR.BasicBlock predecessor : irBasicBlock.getPredecessor()) {
                    basicBlock.addPredecessor(getBasicBlock(predecessor.getName()));
                }
                for (IR.BasicBlock successor : irBasicBlock.getSuccessor()) {
                    basicBlock.addSuccessor(getBasicBlock(successor.getName()));
                }
                if (irBasicBlock.getSuccessor().isEmpty()) {
                    returnBB = basicBlock;
                }
            }
            if (irFunction.getReturnBB() != null) {
                for (IR.BasicBlock predecessor : irFunction.getReturnBB().getPredecessor()) {
                    bb.addPredecessor(getBasicBlock(predecessor.getName()));
                }
                for (IR.BasicBlock successor : irFunction.getReturnBB().getSuccessor()) {
                    bb.addSuccessor(getBasicBlock(successor.getName()));
                }
            }


            for (Parameter param : irFunction.getParameters()) {
                RegisterVirtual registerVirtual = new RegisterVirtual(param.getName());
                CheckAndSetName(registerVirtual.getName(), registerVirtual);
            }

            for (IR.BasicBlock irBasicBlock : irFunction.getBlockList()) {
                for (IRInst inst : irBasicBlock.getInstList()) {
                    if (inst.getResult() != null) {
                        RegisterVirtual registerVirtual = new RegisterVirtual(inst.getResult().getName());
                        CheckAndSetName(registerVirtual.getName(), registerVirtual);
                    }
                }
            }
            if (irFunction.getReturnBB() != null) {
                for (IRInst inst : irFunction.getReturnBB().getInstList()) {
                    if (inst.getResult() != null) {
                        RegisterVirtual registerVirtual = new RegisterVirtual(inst.getResult().getName());
                        CheckAndSetName(registerVirtual.getName(), registerVirtual);
                    }
                }
            }
        }
    }

    public BasicBlock getReturnBB() {
        return returnBB;
    }

    public void addBasicBlock(BasicBlock basicBlock) {
        assert !blockMap.containsKey(basicBlock.getIrIdentifier());
        if (headBB == null) {
            headBB = basicBlock;
        } else {
            tailBB.setNextBB(basicBlock);
            basicBlock.setPrevBB(tailBB);
        }
        tailBB = basicBlock;
        blockMap.put(basicBlock.getIrIdentifier(), basicBlock);
        ++blockNum;
    }

    public BasicBlock getBasicBlock(String name) {
        return blockMap.get(name);
    }

    public IR.Function getIrFunction() {
        return irFunction;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public ArrayList<BasicBlock> getBlockList() {
        ArrayList<BasicBlock> basicBlocks = new ArrayList<>();
        for (BasicBlock cur = headBB; cur != null; cur = cur.getNextBB()) {
            basicBlocks.add(cur);
        }
        return basicBlocks;
    }

    public ArrayList<Instruction> getInstList() {
        ArrayList<Instruction> instructions = new ArrayList<>();
        for (BasicBlock cur = headBB; cur != null; cur = cur.getNextBB()) {
            instructions.addAll(cur.getInstList());
        }
        return instructions;
    }

    public void setStack(Stack stack) {
        assert this.stack == null;
        this.stack = stack;
    }

    public void setHeadBB(BasicBlock headBB) {
        this.headBB = headBB;
    }

    public void setTailBB(BasicBlock tailBB) {
        this.tailBB = tailBB;
    }

    public String getName() {
        return name;
    }

    public Module getModule() {
        return module;
    }

    public Stack getStack() {
        return stack;
    }

    public BasicBlock getHeadBB() {
        return headBB;
    }

    public BasicBlock getTailBB() {
        return tailBB;
    }

    public void CheckAndSetName(String name, RegisterVirtual operand) {
        int tag = 0;
        String tmp = name;
        while (OperandMap.containsKey(tmp)) {
            tmp = name + "_" + tag;
            tag++;
        }
        OperandMap.put(tmp, operand);
        operand.setName(tmp);
    }

    public RegisterVirtual getRV(String name) {
        return OperandMap.get(name);
    }

    public ArrayList<BasicBlock> getDfsList() {
        if (dfsList.size() == 0) {
            dfs(headBB);
        }
        return dfsList;
    }

    private void dfs(BasicBlock basicBlock) {
        dfsList.add(basicBlock);
        visited.add(basicBlock);
        for (BasicBlock bb: basicBlock.getSuccessor()) {
            if (!visited.contains(bb)) {
                dfs(bb);
            }
        }
    }

    public Map<String, RegisterVirtual> getOperandMap() {
        return OperandMap;
    }

    @Override
    public String toString() {
        return name;
    }

    public void accept(CodegenVisitor visitor) {
        visitor.visit(this);
    }
}
