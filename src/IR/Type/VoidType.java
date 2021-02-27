package IR.Type;

public class VoidType extends IRType {
    @Override
    public String toString() {
        return "void";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof VoidType);
    }
}
