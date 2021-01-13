package AST;

public class ForStmt extends StatementNode {
    private ExprNode init;
    private ExprNode cond;
    private ExprNode step;
    private StatementNode statement;

    public ForStmt(ExprNode init, ExprNode cond, ExprNode step, StatementNode statement) {
        this.init = init;
        this.cond = cond;
        this.step = step;
        this.statement = statement;
    }

    public ExprNode getInit() {
        return init;
    }

    public ExprNode getCond() {
        return cond;
    }

    public ExprNode getStep() {
        return step;
    }

    public StatementNode getStatement() {
        return statement;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
