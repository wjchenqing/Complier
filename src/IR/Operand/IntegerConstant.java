package IR.Operand;

import IR.Type.IntegerType;

public class IntegerConstant extends IROper {
    private long value;

    public IntegerConstant(int numberOfBits, long value) {
        super(new IntegerType(numberOfBits));
        this.value = value;
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
}
