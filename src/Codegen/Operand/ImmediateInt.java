package Codegen.Operand;

public class ImmediateInt extends Immediate {
    private int value;

    public ImmediateInt(long value) {
        this.value = (int) value;
    }

    public int getValue() {
        return value;
    }

    public void negation() {
        value = -value;
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof ImmediateInt) && ((ImmediateInt) obj).value == value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public String printCode() {
        return String.valueOf(value);
    }
}
