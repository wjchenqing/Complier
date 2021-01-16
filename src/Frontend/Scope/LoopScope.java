package Frontend.Scope;

import Frontend.Entity.Entity;

import java.util.ArrayList;
import java.util.Map;

public class LoopScope extends Scope {
    public LoopScope(Scope parentScope) {
        super(parentScope);
    }

    @Override
    public boolean inLoopScope() {
        return true;
    }
}
