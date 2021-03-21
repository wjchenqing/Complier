package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Register;

public class Move extends Instruction {
    private final Register rd;
    private final Register rs;

    public Move(BasicBlock basicBlock, Register rd, Register rs) {
        super(basicBlock);
        this.rd = rd;
        this.rs = rs;
    }

    public Register getRd() {
        return rd;
    }

    public Register getRs() {
        return rs;
    }
}
