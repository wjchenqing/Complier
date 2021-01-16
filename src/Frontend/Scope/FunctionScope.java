package Frontend.Scope;

import Frontend.Entity.Entity;

import java.util.ArrayList;
import java.util.Map;

public class FunctionScope extends Scope {
    public FunctionScope(Scope parentScope) {
        super(parentScope);
    }

    @Override
    public boolean inFunctionScope() {
        return true;
    }
}
