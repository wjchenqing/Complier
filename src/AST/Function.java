package AST;

import java.util.ArrayList;

public class Function extends ProgramNode {
    private TypeNode type;
    private String identifier;
    private ArrayList<Variable> params;
    private StatementNode statement;

    public Function(TypeNode type, String identifier, ArrayList<Variable> param_list, StatementNode statement) {
        this.type = type;
        this.identifier = identifier;
        this.params = param_list;
        this.statement = statement;
    }

    public TypeNode getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<Variable> getParams() {
        return params;
    }

    public void addParam (Variable param) {
        params.add(param);
    }

    public StatementNode getStatement() {
        return statement;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
