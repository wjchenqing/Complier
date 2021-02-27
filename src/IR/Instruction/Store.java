package IR.Instruction;

import IR.BasicBlock;
import IR.Operand.IROper;
import IR.Type.PointerType;

public class Store extends IRInst {
    private IROper value;
    private IROper pointer;

    public Store(BasicBlock currentBB, IROper value, IROper pointer) {
        super(currentBB);
        if (!(pointer.getType() instanceof PointerType)) {
            System.exit(-1);
        } else if (!value.getType().equals(((PointerType) pointer.getType()).getType())) {
            System.exit(-1);
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
        return "store " + value.getType().toString() + " " + value.toString() + ", "
                + pointer.getType().toString() + " " + pointer.toString();
    }
}
