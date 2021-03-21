package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Immediate;
import Codegen.Operand.Register;

public class LoadImmediate extends Instruction{
    private final Register rd;
    private final Immediate immediate;

    public LoadImmediate(BasicBlock basicBlock, Register rd, Immediate immediate) {
        super(basicBlock);
        this.rd = rd;
        this.immediate = immediate;
    }

    public Register getRd() {
        return rd;
    }

    public Immediate getImmediate() {
        return immediate;
    }
}
