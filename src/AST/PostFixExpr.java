package AST;

public class PostFixExpr extends ExprNode {
    public enum Operator {postFixIncrease, postFixDecrease}
    private Operator op;
    private ExprNode expr;

    public PostFixExpr(Operator op, ExprNode expr) {
        this.op = op;
        this.expr = expr;
    }

    public Operator getOp() {
        return op;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
