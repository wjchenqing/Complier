package AST;

public class BreakStmt extends StatementNode {
    public BreakStmt() {
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
