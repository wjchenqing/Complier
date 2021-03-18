package IR.Instruction;

import IR.BasicBlock;
import IR.Operand.GlobalVariable;
import IR.Operand.IROper;
import IR.Operand.NullConstant;
import IR.Type.IRType;
import IR.Type.PointerType;
import IR.Type.VoidType;

public class Store extends IRInst {
    private IROper value;
    private IROper pointer;

    public Store(BasicBlock currentBB, IROper value, IROper pointer) {
        super(currentBB);

        if (!(pointer instanceof GlobalVariable)) {
            if (!(pointer.getType() instanceof PointerType)) {
                assert false;
            }
            if (!value.getType().equals(new PointerType(new VoidType()))) {
                if (!value.getType().equals(((PointerType) pointer.getType()).getType())) {
                    assert false;
                }
            }
        }
        this.value = value;
        this.pointer = pointer;
    }

    public IROper getValue() {
        return value;
    }

    public IROper getPointer() {
        return pointer;
    }

    @Override
    public String toString() {
        IRType type1 = (pointer instanceof GlobalVariable) ? new PointerType(pointer.getType()) : pointer.getType();
        IRType type = (value instanceof NullConstant) ? (((PointerType) type1)).getType(): value.getType();
        return "store " + type.toString() + " " + value.toString() + ", "
                + type1.toString() + " " + pointer.toString();
    }
}
