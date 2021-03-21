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
}
