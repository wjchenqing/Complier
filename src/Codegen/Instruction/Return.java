package Codegen.Instruction;

import Codegen.BasicBlock;

public class Return extends Instruction {
    public Return(BasicBlock basicBlock) {
        super(basicBlock);
    }

    @Override
    public void addToUEVarVarKill() {

    }

    @Override
    public String toString() {
        return "ret";
    }

    @Override
    public String printCode() {
        return "\tret";
    }
}
