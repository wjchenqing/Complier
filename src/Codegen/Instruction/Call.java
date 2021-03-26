package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Function;

public class Call extends Instruction {
    private Function callee;

    public Call(BasicBlock basicBlock, Function callee) {
        super(basicBlock);
        this.callee = callee;
    }

    @Override
    public void addToUEVarVarKill() {
    }

    @Override
    public String toString() {
        return "call " + callee.getName();
    }

    @Override
    public String printCode() {
        return "\tcall\t" + callee.getName();
    }
}
