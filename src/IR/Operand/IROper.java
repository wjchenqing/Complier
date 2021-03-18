package IR.Operand;

import IR.Type.IRType;

abstract public class IROper implements Cloneable {
    protected IRType type;

    public IROper(IRType type) {
        this.type = type;
    }

    public IRType getType() {
        return type;
    }

    public void setName(String name) {}

    @Override
    abstract public String toString();

    abstract public boolean isConstant();


}
