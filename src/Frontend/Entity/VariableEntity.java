package Frontend.Entity;

import AST.ExprNode;
import AST.TypeNode;

public class VariableEntity extends Entity {
    private TypeNode type;
    private ExprNode expr;

    public VariableEntity(String name, TypeNode type, ExprNode expr) {
        super(name);
        this.type = type;
        this.expr = expr;
    }

    public TypeNode getType() {
        return type;
    }

    public ExprNode getExpr() {
        return expr;
    }
}
