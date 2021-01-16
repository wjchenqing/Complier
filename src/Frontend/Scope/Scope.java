package Frontend.Scope;

import Frontend.Entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract public class Scope {
    protected Scope parentScope;
    protected ArrayList<Scope> childrenScope;
    protected Map<String, Entity> entityMap;

    public Scope(Scope parentScope) {
        this.parentScope = parentScope;
        this.childrenScope = new ArrayList<>();
        this.entityMap = new HashMap<>();
    }

    public Scope getParentScope() {
        return parentScope;
    }

    public ArrayList<Scope> getChildrenScope() {
        return childrenScope;
    }

    public Map<String, Entity> getEntityMap() {
        return entityMap;
    }

    public void setChildrenScope(ArrayList<Scope> childrenScope) {
        this.childrenScope = childrenScope;
    }

    public void setEntityMap(Map<String, Entity> entityMap) {
        this.entityMap = entityMap;
    }

    public void declareEntity(Entity entity) {
        Entity e = entityMap.get(entity.getName());
        if (e != null) {
            System.exit(-1);
            // Entities with the same name is not allowed in one scope.
            // Question: variable with the same name of a function, should I exit?
        } else {
            entityMap.put(entity.getName(), entity);
        }
    }

    public boolean inBlockScope() {
         return parentScope.inBlockScope();
    }

    public boolean inClassScope() {
        return parentScope.inClassScope();
    }

    public boolean inFunctionScope() {
        return parentScope.inFunctionScope();
    }

    public boolean inLoopScope() {
        return parentScope.inLoopScope();
    }

    public Entity getEntity(String name) {
        Entity entity = entityMap.get(name);
        if (entity != null) {
            return entity;
        } else {
            return parentScope.getEntity(name);
        }
    }
}
