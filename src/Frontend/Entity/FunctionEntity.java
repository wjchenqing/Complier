package Frontend.Entity;

import AST.ExprNode;
import AST.StatementNode;
import AST.TypeNode;

import java.util.ArrayList;

public class FunctionEntity extends Entity {
    private TypeNode returnType;
    private ArrayList<VariableEntity> params;
    private StatementNode body;

    public FunctionEntity(String name, TypeNode returnType, ArrayList<VariableEntity> params, StatementNode body) {
        super(name);
        this.returnType = returnType;
        this.params = params;
        this.body = body;
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    public ArrayList<VariableEntity> getParams() {
        return params;
    }

    public StatementNode getBody() {
        return body;
    }
}
