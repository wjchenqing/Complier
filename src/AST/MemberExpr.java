package AST;

public class MemberExpr extends ExprNode {
    private ExprNode expr;
    private String identifier;

    public MemberExpr(ExprNode expr, String identifier) {
        this.expr = expr;
        this.identifier = identifier;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
