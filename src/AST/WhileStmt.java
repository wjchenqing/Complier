package AST;

public class WhileStmt extends StatementNode {
    private ExprNode expr;
    private StatementNode body;

    public WhileStmt(ExprNode expr, StatementNode body) {
        this.expr = expr;
        this.body = body;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public StatementNode getBody() {
        return body;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
