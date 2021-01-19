package Frontend.Scope;

import AST.PrimitiveType;
import AST.TypeNode;
import Frontend.Entity.Entity;
import Frontend.Entity.FunctionEntity;
import Frontend.Entity.VariableEntity;
import Frontend.Type.Type2;

import java.util.ArrayList;
import java.util.Map;

public class ProgramScope extends Scope {
    public ProgramScope() {
        super(null);
    }

    public void addBuiltInFunction() {
        // void print(string str);
        ArrayList<VariableEntity> param1 = new ArrayList<>();
        param1.add(new VariableEntity("str", new PrimitiveType("string"), null));
        entityMap.put("print", new FunctionEntity("print", new PrimitiveType("void"), param1, null));

        // void println(string str);
        ArrayList<VariableEntity> param2 = new ArrayList<>();
        param2.add(new VariableEntity("str", new PrimitiveType("string"), null));
        entityMap.put("println", new FunctionEntity("println", new PrimitiveType("void"), param2, null));

        // void printInt(int n);
        ArrayList<VariableEntity> param3 = new ArrayList<>();
        param3.add(new VariableEntity("n", new PrimitiveType("int"), null));
        entityMap.put("printInt", new FunctionEntity("printInt", new PrimitiveType("void"), param3, null));

        // void printlnInt(int n);
        ArrayList<VariableEntity> param4 = new ArrayList<>();
        param4.add(new VariableEntity("n", new PrimitiveType("int"), null));
        entityMap.put("printlnInt", new FunctionEntity("printlnInt", new PrimitiveType("void"), param4, null));

        // string getString();
        ArrayList<VariableEntity> param5 = new ArrayList<>();
        entityMap.put("getString", new FunctionEntity("getString", new PrimitiveType("string"), param5, null));

        // int getInt();
        ArrayList<VariableEntity> param6 = new ArrayList<>();
        entityMap.put("getInt", new FunctionEntity("getInt", new PrimitiveType("int"), param6, null));

        // string toString(int i);
        ArrayList<VariableEntity> param7 = new ArrayList<>();
        param7.add(new VariableEntity("i", new PrimitiveType("int"), null));
        entityMap.put("toString", new FunctionEntity("toString", new PrimitiveType("string"), param7, null));
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

    @Override
    public TypeNode getReturnType() {
        return null;
    }

    @Override
    public Type2 getClassType() {
        return null;
    }
}
