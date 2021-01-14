package AST;

import java.util.ArrayList;

public class VariableList extends ProgramNode {
    private ArrayList<Variable> variables;

    public VariableList(ArrayList<Variable> variables) {
        this.variables = variables;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
