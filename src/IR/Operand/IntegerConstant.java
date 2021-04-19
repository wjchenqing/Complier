package IR.Operand;

import IR.Type.IntegerType;

public class IntegerConstant extends IROper {
    private long value;

    public IntegerConstant(long value) {
        super(new IntegerType(32));
        this.value = value;
    }

    @Override
    public String getName() {
        return null;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
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

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof IntegerConstant) && (value == ((IntegerConstant) obj).value);
    }
}
