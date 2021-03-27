package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Immediate;
import Codegen.Operand.Operand;
import Codegen.Operand.Register;
import Codegen.Operand.RegisterVirtual;

public class BinaryInstruction extends Instruction {
    static public enum Name {
        addi, slti, sltiu, xori, ori, andi, slli, srli, srai,
        add, sub, sll, slt, sltu, xor, srl, sra, or, and,
        mul, div, rem,
    }
    private final boolean isImmediateType;
    private final Name name;
    private Register rd;
    private Register rs1;
    private Operand  rs2;

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
            if (!(rs2 instanceof Register)) {
                assert false;
            }
        }
        def.add((RegisterVirtual) rd);
        use.add((RegisterVirtual) rs1);
        if (rs2 instanceof RegisterVirtual) {
            use.add((RegisterVirtual) rs2);
        }
    }

    @Override
    public void replaceDef(RegisterVirtual old, RegisterVirtual n) {
        assert rd == old;
        rd = n;
        super.replaceDef(old, n);
    }

    @Override
    public void replaceUse(RegisterVirtual old, RegisterVirtual n) {
        if (rs1 == old) {
            rs1 = n;
        } else {
            assert rs2 == old;
            rs2 = n;
        }
        super.replaceUse(old, n);
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

    @Override
    public void addToUEVarVarKill() {
        if (!basicBlock.hasVarKill((RegisterVirtual) rs1)) {
            basicBlock.addUEVar((RegisterVirtual) rs1);
        }
        if ((!isImmediateType) && (!basicBlock.hasVarKill((RegisterVirtual) rs2))) {
            basicBlock.addUEVar((RegisterVirtual) rs2);
        }
        basicBlock.addVarKill((RegisterVirtual) rd);
    }

    @Override
    public String toString() {
        return name.name() + " " + rd.toString() + ", " + rs1.toString() + ", " + rs2.toString();
    }

    @Override
    public String printCode() {
        return "\t" + name.name() + "\t" + rd.printCode() + ", " + rs1.printCode() + ", " + rs2.printCode();
    }
}
