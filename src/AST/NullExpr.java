package AST;

public class NullExpr extends ConstExprNode {
    public NullExpr() {
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
