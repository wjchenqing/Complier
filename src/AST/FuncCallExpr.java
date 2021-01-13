package AST;

import java.util.ArrayList;

public class FuncCallExpr extends ExprNode {
    private ExprNode funcExpr;
    private ArrayList<ExprNode> exprNodes;

    public FuncCallExpr(ExprNode funcExpr, ArrayList<ExprNode> exprNodes) {
        this.funcExpr = funcExpr;
        this.exprNodes = exprNodes;
    }

    public ExprNode getFuncExpr() {
        return funcExpr;
    }

    public ArrayList<ExprNode> getExprNodes() {
        return exprNodes;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
