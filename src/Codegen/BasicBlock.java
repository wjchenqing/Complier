package Codegen;

import Codegen.Instruction.Branch;
import Codegen.Instruction.Instruction;
import IR.Instruction.Br;
import IR.Instruction.IRInst;
import IR.Instruction.Ret;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class BasicBlock {
    private final Function function;
    private String name;

    private final String IrIdentifier;
    private final IR.BasicBlock irBasicBlock;

    private BasicBlock prevBB = null;
    private BasicBlock nextBB = null;
    private Instruction headInst = null;
    private Instruction tailInst = null;

    private final ArrayList<Instruction> instructions = new ArrayList<>();

    private final Set<BasicBlock> predecessor = new LinkedHashSet<>();
    private final Set<BasicBlock> successor = new LinkedHashSet<>();

    public BasicBlock(Function function, String name, IR.BasicBlock irBasicBlock) {
        this.function = function;
        this.name = name;
        IrIdentifier = irBasicBlock.getName();
        this.irBasicBlock = irBasicBlock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPredecessor(BasicBlock basicBlock) {
        predecessor.add(basicBlock);
    }

    public void addSuccessor(BasicBlock basicBlock) {
        successor.add(basicBlock);
    }

    public void addInst(Instruction instruction) {
        if (isEmpty()) {
            headInst = instruction;
            tailInst = instruction;
        } else {
            instruction.setPrev(tailInst);
            tailInst.setNext(instruction);
            tailInst = instruction;
        }
    }

    public boolean isEmpty() {
        return headInst == null;
    }

    public ArrayList<Instruction> getInstList() {
        ArrayList<Instruction> instList = new ArrayList<>();
        for (Instruction cur = headInst; cur != null; cur = cur.getNext()) {
            instList.add(cur);
        }
        return instList;
    }

    public Function getFunction() {
        return function;
    }

    public void setPrevBB(BasicBlock prevBB) {
        this.prevBB = prevBB;
    }

    public void setNextBB(BasicBlock nextBB) {
        this.nextBB = nextBB;
    }

    public String getName() {
        return name;
    }

    public String getIrIdentifier() {
        return IrIdentifier;
    }

    public IR.BasicBlock getIrBasicBlock() {
        return irBasicBlock;
    }

    public BasicBlock getPrevBB() {
        return prevBB;
    }

    public BasicBlock getNextBB() {
        return nextBB;
    }

    public Instruction getHeadInst() {
        return headInst;
    }

    public Instruction getTailInst() {
        return tailInst;
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public Set<BasicBlock> getPredecessor() {
        return predecessor;
    }

    public Set<BasicBlock> getSuccessor() {
        return successor;
    }
}
