package AST;

public class ThisExpr extends ExprNode {
    public ThisExpr() {
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
