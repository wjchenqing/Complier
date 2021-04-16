package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
import IR.Operand.IROper;
import IR.Operand.Register;
import IR.Type.IRType;
import IR.Type.PointerType;

public class Alloca extends IRInst {
    private Register result;
    private IRType   type;

    public Alloca(BasicBlock currentBB, Register result, IRType type) {
        super(currentBB);
        if (!result.getType().equals(new PointerType(type))) {
            assert false;
        }
        this.result = result;
        this.type = type;
        defs.add(result);
        result.addDef(this);
        currentBB.getCurrentFunction().defs.add(result);
        currentBB.getCurrentFunction().allocaResults.add(result);
    }

    @Override
    public void replaceUse(IROper o, IROper n) {

    }

    @Override
    public Register getResult() {
        return result;
    }

    public IRType getType() {
        return type;
    }

    @Override
    public String toString() {
        return result.toString() + " = alloca " + type.toString();
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }

}
