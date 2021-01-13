package AST;

import java.util.ArrayList;

public class BlockStmt extends StatementNode {
    private ArrayList<StatementNode> statements;

    public BlockStmt(ArrayList<StatementNode> statements) {
        this.statements = statements;
    }

    public ArrayList<StatementNode> getStatements() {
        return statements;
    }

    public void addStatement (StatementNode statement) {
        statements.add(statement);
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
