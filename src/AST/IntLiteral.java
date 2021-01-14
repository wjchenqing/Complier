package AST;

public class IntLiteral extends ConstExprNode {
    private long val;

    public long getVal() {
        return val;
    }

    public IntLiteral(long val) {
        this.val = val;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
