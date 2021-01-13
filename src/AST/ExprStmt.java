package AST;

public class ExprStmt extends StatementNode {
    private ExprNode expr;

    public ExprStmt(ExprNode expr) {
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
