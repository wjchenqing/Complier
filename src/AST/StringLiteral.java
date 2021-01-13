package AST;

public class StringLiteral extends ConstExprNode {
    private String val;

    public StringLiteral(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
