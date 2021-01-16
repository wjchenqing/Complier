package Frontend.Scope;

import Frontend.Entity.Entity;

import java.util.ArrayList;
import java.util.Map;

public class ProgramScope extends Scope {
    public ProgramScope() {
        super(null);
    }

    @Override
    public boolean inBlockScope() {
        return false;
    }

    @Override
    public boolean inClassScope() {
        return false;
    }

    @Override
    public boolean inFunctionScope() {
        return false;
    }

    @Override
    public boolean inLoopScope() {
        return false;
    }

    @Override
    public Entity getEntity(String name) {
        return entityMap.get(name);
    }
}
