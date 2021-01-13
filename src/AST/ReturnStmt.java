package AST;

public class ReturnStmt extends StatementNode {
    private ExprNode expr;

    public ReturnStmt(ExprNode expr) {
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
