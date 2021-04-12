package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
import IR.Operand.BoolConstant;
import IR.Operand.IROper;
import IR.Operand.IntegerConstant;
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
        if ((op1 instanceof IntegerConstant && op2 instanceof IntegerConstant)) {
            boolean flag = true;
            switch (op) {
                case add :
                    result.replaceUse(new IntegerConstant(((IntegerConstant) op1).getValue() + ((IntegerConstant) op2).getValue()));
                    break;
                case sub :
                    result.replaceUse(new IntegerConstant(((IntegerConstant) op1).getValue() - ((IntegerConstant) op2).getValue()));
                    break;
                case mul :
                    result.replaceUse(new IntegerConstant(((IntegerConstant) op1).getValue() * ((IntegerConstant) op2).getValue()));
                    break;
                case sdiv:
                    if (((IntegerConstant) op2).getValue() == 0) {
                        flag = false;
                        break;
                    }
                    result.replaceUse(new IntegerConstant(((IntegerConstant) op1).getValue() / ((IntegerConstant) op2).getValue()));
                    break;
                case srem:
                    result.replaceUse(new IntegerConstant(((IntegerConstant) op1).getValue() % ((IntegerConstant) op2).getValue()));
                    break;
                case shl :
                    result.replaceUse(new IntegerConstant(((IntegerConstant) op1).getValue() << ((IntegerConstant) op2).getValue()));
                    break;
                case ashr:
                    result.replaceUse(new IntegerConstant(((IntegerConstant) op1).getValue() >> ((IntegerConstant) op2).getValue()));
                    break;
                case and :
                    result.replaceUse(new IntegerConstant(((IntegerConstant) op1).getValue() & ((IntegerConstant) op2).getValue()));
                    break;
                case or  :
                    result.replaceUse(new IntegerConstant(((IntegerConstant) op1).getValue() | ((IntegerConstant) op2).getValue()));
                    break;
                case xor :
                    result.replaceUse(new IntegerConstant(((IntegerConstant) op1).getValue() ^ ((IntegerConstant) op2).getValue()));
            }
            if (flag) {
                this.deleteInst();
            }
        } else if (op1 instanceof BoolConstant && op2 instanceof BoolConstant) {
            switch (op) {
                case and:
                    result.replaceUse(new BoolConstant(((BoolConstant) op1).getValue() & ((BoolConstant) op2).getValue()));
                    break;
                case or :
                    result.replaceUse(new BoolConstant(((BoolConstant) op1).getValue() | ((BoolConstant) op2).getValue()));
                    break;
                case xor:
                    result.replaceUse(new BoolConstant(((BoolConstant) op1).getValue() ^ ((BoolConstant) op2).getValue()));
                    break;
                default : {
                    assert false;
                }
            }
            this.deleteInst();
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
