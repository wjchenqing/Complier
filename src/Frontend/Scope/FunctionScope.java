package Frontend.Scope;

import AST.TypeNode;
import Frontend.Entity.Entity;

import java.util.ArrayList;
import java.util.Map;

public class FunctionScope extends Scope {
    private TypeNode functionReturnType;

    public FunctionScope(Scope parentScope, TypeNode typeNode) {
        super(parentScope);
        functionReturnType = typeNode;
    }

    @Override
    public boolean inFunctionScope() {
        return true;
    }

    @Override
    public TypeNode getReturnType() {
        return functionReturnType;
    }
}
