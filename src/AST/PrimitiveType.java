package AST;

public class PrimitiveType extends TypeNode {
    public PrimitiveType(String identifier) {
        super(identifier);
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
