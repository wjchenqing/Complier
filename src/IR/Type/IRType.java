package IR.Type;

import IR.Operand.IROper;
import IR.Operand.NullConstant;

abstract public class IRType {
    @Override
    abstract public String toString();

    @Override
    abstract public boolean equals(Object obj);

    public IROper defaultOperand() {
        assert false;
        return new NullConstant();
    }

    abstract public int getByte();
}
