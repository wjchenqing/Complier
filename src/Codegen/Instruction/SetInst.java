package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Register;

public class SetInst extends Instruction {
    static public enum Name {
        seqz, snez, sltz, sgtz
    }
    private final Name name;
    private final Register rd;
    private final Register rs;

    public SetInst(BasicBlock basicBlock, Name name, Register rd, Register rs) {
        super(basicBlock);
        this.name = name;
        this.rd = rd;
        this.rs = rs;
    }

    public Name getName() {
        return name;
    }

    public Register getRd() {
        return rd;
    }

    public Register getRs() {
        return rs;
    }
}
