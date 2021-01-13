package AST;

public class BinaryExpr extends ExprNode {
    public enum Operator {
        multiply, division, mod,
        plus, sub,
        shiftLeft, shiftRight,
        less, greater, lessEqual, greaterEqual,
        equal, notEqual,
        bitWiseAnd,
        bitWiseOr,
        bitWiseXor,
        and,
        or,
        assign
    }
    private Operator op;
    private ExprNode opd1;
    private ExprNode opd2;

    public BinaryExpr(Operator op, ExprNode opd1, ExprNode opd2) {
        this.op = op;
        this.opd1 = opd1;
        this.opd2 = opd2;
    }

    public Operator getOp() {
        return op;
    }

    public ExprNode getOpd1() {
        return opd1;
    }

    public ExprNode getOpd2() {
        return opd2;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
