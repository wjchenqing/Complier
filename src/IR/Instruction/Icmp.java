package IR.Instruction;

import IR.BasicBlock;
import IR.Operand.IROper;
import IR.Operand.NullConstant;
import IR.Operand.Register;
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
    }

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
}
