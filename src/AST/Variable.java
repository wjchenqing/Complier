package AST;

public class Variable extends ProgramNode {
    private TypeNode type;
    private String identifier;

    public Variable(TypeNode type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public TypeNode getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
