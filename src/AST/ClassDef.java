package AST;

import java.util.ArrayList;

public class ClassDef extends ProgramNode {
    private String identifier;
    private ArrayList<Variable> variables;
    private Function constructor;
    private ArrayList<Function> functions;

    public ClassDef(String identifier, ArrayList<Variable> variables, Function constructor, ArrayList<Function> functions) {
        this.identifier = identifier;
        this.variables = variables;
        this.constructor = constructor;
        this.functions = functions;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public void addVariable (Variable variable) {
        variables.add(variable);
    }

    public Function getConstructor() {
        return constructor;
    }

    public ArrayList<Function> getFunctions() {
        return functions;
    }

    public void addFunction (Function function) {
        functions.add(function);
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
