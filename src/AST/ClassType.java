package AST;

public class ClassType extends TypeNode {
    public ClassType(String identifier) {
        super(identifier);
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
