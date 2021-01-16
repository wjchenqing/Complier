package Frontend.Scope;

import Frontend.Entity.Entity;

import java.util.ArrayList;
import java.util.Map;

public class BlockScope extends Scope {
    public BlockScope(Scope parentScope) {
        super(parentScope);
    }

    @Override
    public boolean inBlockScope() {
        return true;
    }
}
