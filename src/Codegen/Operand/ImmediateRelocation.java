package Codegen.Operand;

public class ImmediateRelocation extends Immediate {
    public enum ImmType {
        hi, lo
    }
    private final ImmType immType;
    private final GlobalVar value;

    public ImmediateRelocation(ImmType immType, GlobalVar value) {
        this.immType = immType;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof ImmediateRelocation)
                && (immType == ((ImmediateRelocation) obj).immType)
                && (value == ((ImmediateRelocation) obj).value));
    }

    public ImmType getImmType() {
        return immType;
    }

    public GlobalVar getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "%" + immType.name() + "(" + value.toString() + ")";
    }

    @Override
    public String printCode() {
        return "%" + immType.name() + "(" + value.toString() + ")";
    }
}
