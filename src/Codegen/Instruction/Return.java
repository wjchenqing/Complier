package Codegen.Instruction;

import Codegen.BasicBlock;

public class Return extends Instruction {
    public Return(BasicBlock basicBlock) {
        super(basicBlock);
    }

    @Override
    public void addToUEVarVarKill() {

    }
}
