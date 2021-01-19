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

    public FunctionEntity getConstructor() {
        return constructor;
    }

    public ArrayList<FunctionEntity> getMethods() {
        return methods;
    }

}
