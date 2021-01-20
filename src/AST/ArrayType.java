package AST;

public class ArrayType extends TypeNode {
    private TypeNode baseType;
    private int dim;

    public ArrayType(TypeNode type) {
        super(type.getIdentifier());
        if (type instanceof ArrayType) {
            baseType = ((ArrayType) type).baseType;
            dim = ((ArrayType) type).dim + 1;
        } else {
            baseType = type;
            dim = 1;
        }
    }

    public TypeNode getBaseType() {
        return baseType;
    }

    public int getDim() {
        return dim;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
