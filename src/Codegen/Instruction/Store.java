package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Addr;
import Codegen.Operand.Register;

public class Store extends Instruction {
    static public enum Name {
        sb, sw
    }
    private final Name name;
    private final Register rd;
    private final Addr addr;

    public Store(BasicBlock basicBlock, Name name, Register rd, Addr addr) {
        super(basicBlock);
        this.name = name;
        this.rd = rd;
        this.addr = addr;
    }

    public Name getName() {
        return name;
    }

    public Register getRd() {
        return rd;
    }

    public Addr getAddr() {
        return addr;
    }
}
