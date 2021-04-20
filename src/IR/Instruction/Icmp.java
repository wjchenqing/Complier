package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
import IR.Operand.*;
import IR.Type.IRType;
import IR.Type.IntegerType;
import IR.Type.PointerType;

public class Icmp extends IRInst {
    public enum Condition {
        eq,     //equal
        ne,     //not equal
        sgt,    //signed greater than
        sge,    //signed greater or equal
        slt,    //signed less than
        sle,    //signed less or equal
    }

    private Register result;
    private Condition cond;
    private IRType type;
    private IROper op1;
    private IROper op2;

    public Icmp(BasicBlock currentBB, Register result, Condition cond, IRType type, IROper op1, IROper op2) {
        super(currentBB);
        assert op1.getType().equals(type) || ((op1 instanceof NullConstant) && (type instanceof PointerType));
        assert op2.getType().equals(type) || ((op2 instanceof NullConstant) && (type instanceof PointerType));
        assert result.getType().equals(new IntegerType(1));
        this.result = result;
        this.cond = cond;
        this.type = type;
        this.op1 = op1;
        this.op2 = op2;
        defs.add(result);
        uses.add(op1);
        uses.add(op2);
        result.addDef(this);
        currentBB.getCurrentFunction().defs.add(result);
        op1.addUse(this);
        op2.addUse(this);
    }

    @Override
    public void replaceUse(IROper o, IROper n) {
        boolean changed = false;
        if (op1 == o) {
            uses.remove(op1);
            uses.add(n);
            op1 = n;
            n.addUse(this);
            changed = true;
        }
        if (op2 == o) {
            uses.remove(op2);
            uses.add(n);
            op2 = n;
            n.addUse(this);
            changed = true;
        }
        if (!changed) {
            return;
        }
        if (op1 instanceof IntegerConstant && op2 instanceof IntegerConstant) {
            switch (cond) {
                case eq :
                    result.replaceUse(new BoolConstant(((IntegerConstant) op1).getValue() == ((IntegerConstant) op2).getValue()));
                    break;
                case ne :
                    result.replaceUse(new BoolConstant(((IntegerConstant) op1).getValue() != ((IntegerConstant) op2).getValue()));
                    break;
                case sgt:
                    result.replaceUse(new BoolConstant(((IntegerConstant) op1).getValue() > ((IntegerConstant) op2).getValue()));
                    break;
                case sge:
                    result.replaceUse(new BoolConstant(((IntegerConstant) op1).getValue() >= ((IntegerConstant) op2).getValue()));
                    break;
                case slt:
                    result.replaceUse(new BoolConstant(((IntegerConstant) op1).getValue() < ((IntegerConstant) op2).getValue()));
                    break;
                case sle:
                    result.replaceUse(new BoolConstant(((IntegerConstant) op1).getValue() <= ((IntegerConstant) op2).getValue()));
            }
            this.deleteInst();
        } else if (op1 instanceof BoolConstant && op2 instanceof BoolConstant) {
            switch (cond) {
                case eq :
                    result.replaceUse(new BoolConstant(((BoolConstant) op1).getValue() == ((BoolConstant) op2).getValue()));
                    break;
                case ne :
                    result.replaceUse(new BoolConstant(((BoolConstant) op1).getValue() != ((BoolConstant) op2).getValue()));
                    break;
                default : {
                    assert false;
                }
            }
            this.deleteInst();
        }
    }

    public void setForRISCV () {
        if (op2 instanceof BoolConstant) {
            return;
        }
        if (!(op2 instanceof  IntegerConstant)) {
            assert op2 instanceof IntegerConstant;
        }
        if (cond == Condition.sle) {
            cond = Condition.slt;
            op2 = new IntegerConstant(((IntegerConstant) op2).getValue() + 1);
        } else if (cond == Condition.sge) {
            cond = Condition.sgt;
            op2 = new IntegerConstant(((IntegerConstant) op2).getValue() - 1);
        }
    }

    @Override
    public Register getResult() {
        return result;
    }

    public Condition getCond() {
        return cond;
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
        return result.toString() + " = icmp " + cond.name() + " " + type.toString() + " " + op1.toString() + ", " + op2.toString();
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
