package AST;

import java.util.ArrayList;

public class VarDefStmt extends StatementNode {
    private ArrayList<Variable> variables;

    public VarDefStmt(ArrayList<Variable> variables) {
        this.variables = variables;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public void addVariable (Variable variable) {
        variables.add(variable);
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
