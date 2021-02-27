package IR.Instruction;

import IR.BasicBlock;
import IR.Operand.IROper;
import IR.Operand.Register;
import IR.Type.IRType;

public class BinaryOperation extends IRInst {
    public enum BinaryOp {
        add, sub, mul, sdiv, srem,
        shl, ashr, and, or, xor
    }

    private Register result;
    private BinaryOp op;
    private IRType   type;
    private IROper   op1;
    private IROper   op2;

    public BinaryOperation(BasicBlock currentBB, Register result, BinaryOp op, IRType type, IROper op1, IROper op2) {
        super(currentBB);
        if (!op1.getType().equals(type) || !op2.getType().equals(type)) {
            System.exit(-1);
        }
        this.result = result;
        this.op = op;
        this.type = type;
        this.op1 = op1;
        this.op2 = op2;
    }

    public Register getResult() {
        return result;
    }

    public BinaryOp getOp() {
        return op;
    }

    public IRType getType() {
        return type;
    }

    public IROper getOp1() {
        return op1;
    }

    public IROper getOp2() {
        return op2;
    }

    @Override
    public String toString() {
        return result.toString() + " = " + op.name() + " " + type.toString() + " " + op1.toString() + ", " + op2.toString();
    }
}
