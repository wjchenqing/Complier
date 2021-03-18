package IR.Instruction;

import IR.BasicBlock;
import IR.Operand.GlobalVariable;
import IR.Operand.IROper;
import IR.Operand.Register;
import IR.Type.IRType;
import IR.Type.PointerType;

public class Load extends IRInst {
    private Register result;
    private IRType   type;
    private IROper   pointer;

    public Load(BasicBlock currentBB, Register result, IRType type, IROper pointer) {
        super(currentBB);
        if (!(pointer instanceof GlobalVariable)) {
            if (!(pointer.getType() instanceof PointerType)) {
                assert false;
            } else if (!((PointerType) pointer.getType()).getType().equals(type)) {
                assert false;
            } else if (!result.getType().equals(type)) {
                assert false;
            }
        } else {
            if (!(pointer.getType().equals(type))) {
                assert false;
            } else if (!result.getType().equals(type)) {
                assert false;
            }
        }
        this.result = result;
        this.type = type;
        this.pointer = pointer;
    }

    public Register getResult() {
        return result;
    }

    public IRType getType() {
        return type;
    }

    public IROper getPointer() {
        return pointer;
    }

    @Override
    public String toString() {
        IRType type1 = (pointer instanceof GlobalVariable) ? new PointerType(pointer.getType()) : pointer.getType();
        return result.toString() + " = load " + type.toString() + ", " + type1.toString() + " " + pointer.toString();
    }
}
