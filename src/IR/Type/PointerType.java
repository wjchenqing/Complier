package IR.Type;

import IR.Operand.IROper;
import IR.Operand.NullConstant;

public class PointerType extends IRType {
    private final IRType type;

    public PointerType(IRType type) {
        this.type = type;
    }

    public IRType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString() + "*";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PointerType) && (type.equals(((PointerType) obj).getType()));
    }

    @Override
    public IROper defaultOperand() {
        return new NullConstant();
    }

    @Override
    public int getByte() {
        return 8;
    }
}
