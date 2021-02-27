package IR.Instruction;

import IR.BasicBlock;
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
        if (!(pointer.getType() instanceof PointerType)) {
            System.exit(-1);
        } else if (!((PointerType) pointer.getType()).getType().equals(type)){
            System.exit(-1);
        } else if (!result.getType().equals(type)) {
            System.exit(-1);
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
        return result.toString() + " = load " + type.toString() + ", " + pointer.getType().toString() + " " + pointer.toString();
    }
}
