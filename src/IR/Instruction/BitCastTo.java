package IR.Instruction;

import IR.BasicBlock;
import IR.Operand.IROper;
import IR.Operand.Register;
import IR.Type.IRType;
import IR.Type.PointerType;

public class BitCastTo extends IRInst {
    private final Register result;
    private final IROper   value;
    private final IRType   targetType;

    public BitCastTo(BasicBlock currentBB, Register result, IROper value, IRType targetType) {
        super(currentBB);
        if (!(value.getType() instanceof PointerType) || !(targetType instanceof PointerType)) {
            assert false;
        }
        this.result = result;
        this.value = value;
        this.targetType = targetType;
    }

    public Register getResult() {
        return result;
    }

    public IROper getValue() {
        return value;
    }

    public IRType getTargetType() {
        return targetType;
    }

    @Override
    public String toString() {
        return result.toString() + " = bitcast "
                + value.getType().toString() + " " + value.toString() + " to " + targetType.toString();
    }
}
