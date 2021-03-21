package Codegen.Operand;

public class Addr extends Operand {
    private final boolean isStackLocation;
    private final Register base;
    private final Immediate offset;

    public Addr(boolean isStackLocation, Register base, Immediate offset) {
        this.isStackLocation = isStackLocation;
        if (isStackLocation) {
            assert base == null;
        } else {
            assert base != null;
        }
        this.base = base;
        this.offset = offset;
    }

    public Register getBase() {
        return base;
    }

    public Immediate getOffset() {
        return offset;
    }

    public boolean isStackLocation() {
        return isStackLocation;
    }
}
