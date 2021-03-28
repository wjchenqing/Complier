package Codegen.Operand;

public class Addr extends Operand {
    private final boolean isStackLocation;
    private String name;
    private Register base;
    private Object offset;

    public Addr(boolean isStackLocation, Register base, Immediate offset) {
        this.isStackLocation = isStackLocation;
        if (isStackLocation) {
            name = base.getName();
            this.base = null;
            this.offset = -1;
        } else {
            assert base != null;
            this.base = base;
            this.offset = offset;
        }
    }

    public void setBase(Register base) {
        this.base = base;
    }

    public String getName() {
        return name;
    }

    public Register getBase() {
        return base;
    }

    public void setStackLocation(int i) {
        assert isStackLocation;
        offset = i;
    }

    public int getStackLocationOffset() {
        assert isStackLocation;
        return (int) offset;
    }

    public Immediate getOffset() {
        assert !isStackLocation;
        return (Immediate) offset;
    }

    public void setOffset(int offset) {
        assert isStackLocation;
//        System.out.println("set offset: " + name);
        this.offset = offset;
    }

    public boolean isStackLocation() {
        return isStackLocation;
    }

    @Override
    public String toString() {
        if (isStackLocation) {
            return name + "(sp)";
        } else {
            return offset.toString() + "(" + base.toString() + ")";
        }
    }

    @Override
    public String printCode() {
        if (isStackLocation) {
            return offset + "(sp)";
        } else {
            return ((Immediate) offset).printCode() + "(" + base.printCode() + ")";
        }
    }
}
