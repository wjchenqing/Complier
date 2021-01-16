package Frontend.Scope;

import Frontend.Entity.Entity;

import java.util.ArrayList;
import java.util.Map;

public class ClassScope extends Scope {
    public ClassScope(Scope parentScope) {
        super(parentScope);
    }

    @Override
    public boolean inClassScope() {
        return true;
    }
}
