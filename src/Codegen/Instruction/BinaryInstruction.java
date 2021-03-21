package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Immediate;
import Codegen.Operand.Operand;
import Codegen.Operand.Register;

public class BinaryInstruction extends Instruction {
    static public enum Name {
        addi, slti, sltiu, xori, ori, andi, slli, srli, srai,
        add, sub, sll, slt, sltu, xor, srl, sra, or, and,
        mul, div, rem,
        seqz, snez, sltz, sgtz
    }
    private final boolean isImmediateType;
    private final Name name;
    private final Register rd;
    private final Register rs1;
    private final Operand  rs2;

    public BinaryInstruction(BasicBlock basicBlock, Name name, boolean isImmediateType, Register rd, Register rs1, Operand rs2) {
        super(basicBlock);
        this.isImmediateType = isImmediateType;
        this.name = name;
        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = rs2;
        if (isImmediateType) {
            assert rs2 instanceof Immediate;
        } else {
            assert rs2 instanceof Register;
        }
    }

    public boolean isImmediateType() {
        return isImmediateType;
    }

    public Name getName() {
        return name;
    }

    public Register getRd() {
        return rd;
    }

    public Register getRs1() {
        return rs1;
    }

    public Operand getRs2() {
        return rs2;
    }
}
