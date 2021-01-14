package AST;

public class Variable extends ProgramNode {
    private TypeNode type;
    private String identifier;
    private ExprNode expr;

    public Variable(TypeNode type, String identifier, ExprNode expr) {
        this.type = type;
        this.identifier = identifier;
        this.expr = expr;
    }

    public TypeNode getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
