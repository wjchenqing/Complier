package Frontend.Scope;

import AST.TypeNode;
import Frontend.Entity.Entity;
import Frontend.Entity.FunctionEntity;
import Frontend.Entity.VariableEntity;
import Frontend.Type.Type2;

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
        if (entityMap.containsKey(entity.getName())) {
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
        if (entityMap.containsKey(name) &&
                (entity instanceof VariableEntity
                        || ((FunctionEntity) entity).getReturnType() != null)) {
            return entity;
        } else if (parentScope != null) {
            return parentScope.getEntity(name);
        } else {
            return null;
        }
    }

    public boolean IsMethod(String name) {
        if (entityMap.containsKey(name) && entityMap.get(name) instanceof FunctionEntity) {
            return (this instanceof ClassScope);
        } else if (parentScope != null) {
            return parentScope.IsMethod(name);
        } else {
            return false;
        }
    }

    public boolean IsGlobalVariable(String name) {
        if (entityMap.containsKey(name) && entityMap.get(name) instanceof VariableEntity) {
            return (this instanceof ProgramScope);
        } else if (parentScope != null) {
            return parentScope.IsMethod(name);
        } else {
            return false;
        }
    }

    public FunctionEntity getAnyFunctionEntity(String name) {
        if (entityMap.containsKey(name) && entityMap.get(name) instanceof FunctionEntity) {
            return (FunctionEntity) entityMap.get(name);
        } else if (parentScope != null) {
            return parentScope.getAnyFunctionEntity(name);
        } else {
            return null;
        }
    }

    public TypeNode getReturnType() {
        return parentScope.getReturnType();
    }

    public Type2 getClassType() {
        return parentScope.getClassType();
    }
}
