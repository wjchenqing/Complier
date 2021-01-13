package AST;

import java.util.ArrayList;

public class NewExpr extends ExprNode {
    private TypeNode type;
    private ArrayList<ExprNode> exprPerDim;
    private int dim;

    public NewExpr(TypeNode type, ArrayList<ExprNode> exprPerDim, int dim) {
        this.type = type;
        this.exprPerDim = exprPerDim;
        this.dim = dim;
    }

    public TypeNode getType() {
        return type;
    }

    public ArrayList<ExprNode> getExprPerDim() {
        return exprPerDim;
    }

    public int getDim() {
        return dim;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
