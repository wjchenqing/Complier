package IR.Operand;

import IR.Type.IRType;

abstract public class IROper {
    protected IRType type;

    public IROper(IRType type) {
        this.type = type;
    }

    public IRType getType() {
        return type;
    }

    @Override
    abstract public String toString();
}
