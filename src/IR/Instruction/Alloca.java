package IR.Instruction;

import IR.BasicBlock;
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
    }

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
}
