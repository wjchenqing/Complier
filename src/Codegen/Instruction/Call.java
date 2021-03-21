package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Function;

public class Call extends Instruction {
    private Function callee;

    public Call(BasicBlock basicBlock, Function callee) {
        super(basicBlock);
        this.callee = callee;
    }
}
