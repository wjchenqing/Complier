package AST;

public class IfStmt extends StatementNode {
    private ExprNode condition;
    private StatementNode thenBody;
    private StatementNode elseBody;

    public IfStmt(ExprNode condition, StatementNode thenBody, StatementNode elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    public ExprNode getCondition() {
        return condition;
    }

    public StatementNode getThenBody() {
        return thenBody;
    }

    public StatementNode getElseBody() {
        return elseBody;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
