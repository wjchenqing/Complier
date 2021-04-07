package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
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
            assert false;
        }
        this.result = result;
        this.op = op;
        this.type = type;
        this.op1 = op1;
        this.op2 = op2;
        defs.add(result);
        uses.add(op1);
        uses.add(op2);
        result.addDef(this);
        op1.addUse(this);
        op2.addUse(this);
    }

    @Override
    public void replaceUse(IROper o, IROper n) {
        if (op1 == o) {
            uses.remove(op1);
            uses.add(n);
            op1 = n;
            n.addUse(this);
        }
        if (op2 == o) {
            uses.remove(op2);
            uses.add(n);
            op2 = n;
            n.addUse(this);
        }
    }

    @Override
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

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
