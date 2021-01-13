package AST;

public class SubScriptExpr extends ExprNode {
    private ExprNode nameExpr;
    private ExprNode dimExpr;

    public SubScriptExpr(ExprNode nameExpr, ExprNode dimExpr) {
        this.nameExpr = nameExpr;
        this.dimExpr = dimExpr;
    }

    public ExprNode getNameExpr() {
        return nameExpr;
    }

    public ExprNode getDimExpr() {
        return dimExpr;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
