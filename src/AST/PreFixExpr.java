package AST;

public class PreFixExpr extends ExprNode {
    public enum Operator {preFixIncrease, preFixDecrease, preFixPlus, preFixSub, negation, bitwiseComplement}
    private Operator op;
    private ExprNode expr;

    public PreFixExpr(Operator op, ExprNode expr) {
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
