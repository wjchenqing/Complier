package AST;

public class ArrayType extends TypeNode {
    private TypeNode baseType;
    private int dim;

    public ArrayType(String identifier, TypeNode type) {
        super(identifier);
        if (type instanceof ArrayType) {
            baseType = ((ArrayType) type).baseType;
            dim = ((ArrayType) type).dim;
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
