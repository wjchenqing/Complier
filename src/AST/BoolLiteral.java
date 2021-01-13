package AST;

public class BoolLiteral extends ConstExprNode {
    private boolean val;

    public BoolLiteral(boolean val) {
        this.val = val;
    }

    public boolean isVal() {
        return val;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
