package AST;

public class IntLiteral extends ConstExprNode {
    private int val;

    public int getVal() {
        return val;
    }

    public IntLiteral(int val) {
        this.val = val;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
