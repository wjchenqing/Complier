package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Register;

public class Branch extends Instruction {
    static public enum Name {
        beq,  bne,  ble,  bge,  blt,  bgt
    }
    private final Name name;
    private final Register rs1;
    private final Register rs2;
    private final BasicBlock destination;

    public Branch(BasicBlock basicBlock, Name name, Register rs1, Register rs2, BasicBlock destination) {
        super(basicBlock);
        this.name = name;
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.destination = destination;
    }

    public Name getName() {
        return name;
    }

    public Register getRs1() {
        return rs1;
    }

    public Register getRs2() {
        return rs2;
    }

    public BasicBlock getDestination() {
        return destination;
    }
}
