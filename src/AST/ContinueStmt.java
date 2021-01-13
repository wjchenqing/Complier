package AST;

public class ContinueStmt extends StatementNode {
    public ContinueStmt() {
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
