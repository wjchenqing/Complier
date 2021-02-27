package IR.Type;

public class ArrayType extends IRType {
    private final int elements;
    private final IRType elementType;

    public ArrayType(int elements, IRType elementType) {
        this.elements = elements;
        this.elementType = elementType;
    }

    public int getElements() {
        return elements;
    }

    public IRType getElementType() {
        return elementType;
    }

    @Override
    public String toString() {
        return "[" + elements + " x " + elementType.toString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ArrayType)
                && (elements == ((ArrayType) obj).elements)
                && elementType.equals(((ArrayType) obj).elementType);
    }
}
