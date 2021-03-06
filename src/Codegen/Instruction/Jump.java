package Codegen.Instruction;

import Codegen.BasicBlock;

public class Jump extends Instruction {
    private final BasicBlock destination;

    public Jump(BasicBlock basicBlock, BasicBlock destination) {
        super(basicBlock);
        this.destination = destination;
    }

    public BasicBlock getDestination() {
        return destination;
    }

    @Override
    public void addToUEVarVarKill() {

    }

    @Override
    public String toString() {
        return "j " + destination.toString();
    }

    @Override
    public String printCode() {
        return "\tj\t" + destination.printCode();
    }
}
