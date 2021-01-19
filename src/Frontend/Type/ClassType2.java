package Frontend.Type;

import Frontend.Entity.FunctionEntity;
import Frontend.Entity.VariableEntity;

import java.util.ArrayList;

public class ClassType2 extends Type2 {
    private ArrayList<VariableEntity> members;
    private FunctionEntity constructor;
    private ArrayList<FunctionEntity> methods;

    public ClassType2(String name, ArrayList<VariableEntity> members, FunctionEntity constructor, ArrayList<FunctionEntity> methods) {
        typeName = name;
        this.members = members;
        this.constructor = constructor;
        this.methods = methods;
    }

    public ArrayList<VariableEntity> getMembers() {
        return members;
    }

    public VariableEntity getMember(String name) {
        for (VariableEntity member: members) {
            if (member.getName().equals(name))
                return member;
        }
        return null;
    }

    public FunctionEntity getConstructor() {
        return constructor;
    }

    public ArrayList<FunctionEntity> getMethods() {
        return methods;
    }

    public FunctionEntity getMethod(String name) {
        for (FunctionEntity method: methods) {
            if (method.getName().equals(name))
                return method;
        }
        return null;
    }

    public boolean hasMember(String identifier) {
        for (VariableEntity member: members) {
            if (member.getName().equals(identifier))
                return true;
        }
        return false;
    }

    public boolean hasMethod(String identifier) {
        for (FunctionEntity method: methods) {
            if (method.getName().equals(identifier))
                return true;
        }
        return false;
    }
}
