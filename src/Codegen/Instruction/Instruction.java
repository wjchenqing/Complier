package Codegen.Instruction;

import Codegen.BasicBlock;

abstract public class Instruction {
    private final BasicBlock basicBlock;
    private Instruction prev = null;
    private Instruction next = null;

    public Instruction(BasicBlock basicBlock) {
        this.basicBlock = basicBlock;
    }

    public BasicBlock getBasicBlock() {
        return basicBlock;
    }

    public Instruction getPrev() {
        return prev;
    }

    public Instruction getNext() {
        return next;
    }

    public void setPrev(Instruction prev) {
        this.prev = prev;
    }

    public void setNext(Instruction next) {
        this.next = next;
    }
}
