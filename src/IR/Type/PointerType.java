package IR.Type;

public class PointerType extends IRType {
    private final IRType type;

    public PointerType(IRType type) {
        this.type = type;
    }

    public IRType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString() + "*";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PointerType) && (type == ((PointerType) obj).type);
    }
}
