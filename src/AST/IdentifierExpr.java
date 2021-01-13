package AST;

public class IdentifierExpr extends ExprNode {
    private String identifier;

    public IdentifierExpr(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
