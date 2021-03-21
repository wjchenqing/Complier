package IR.Operand;

import IR.Type.IntegerType;

public class BoolConstant extends IROper {
    private boolean value;

    public BoolConstant(boolean value) {
        super(new IntegerType(1));
        this.value = value;
    }

    @Override
    public String getName() {
        return null;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
