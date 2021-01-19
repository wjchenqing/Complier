package Frontend.Type;

import AST.PrimitiveType;
import Frontend.Entity.FunctionEntity;
import Frontend.Entity.VariableEntity;

import java.util.ArrayList;

public class StringType2 extends Type2 {
    private ArrayList<FunctionEntity> methods = new ArrayList<>();

    public StringType2() {
        typeName = "string";
        // int length();
        ArrayList<VariableEntity> param1 = new ArrayList<>();
        methods.add(new FunctionEntity("length", new PrimitiveType("int"), param1, null));

        // string substring(int left, int right);
        ArrayList<VariableEntity> param2 = new ArrayList<>();
        param2.add(new VariableEntity("left", new PrimitiveType("int"), null));
        param2.add(new VariableEntity("right", new PrimitiveType("int"), null));
        methods.add(new FunctionEntity("length", new PrimitiveType("int"), param2, null));

        // int parseInt();
        ArrayList<VariableEntity> param3 = new ArrayList<>();
        methods.add(new FunctionEntity("parseInt", new PrimitiveType("int"), param3, null));

        // int ord(int pos)
        ArrayList<VariableEntity> param4 = new ArrayList<>();
        param4.add(new VariableEntity("pos", new PrimitiveType("int"), null));
        methods.add(new FunctionEntity("ord", new PrimitiveType("int"), param4, null));
    }

    public boolean hasMethod (String name) {
        for (FunctionEntity functionEntity: methods) {
            if (functionEntity.getName().equals(name))
                return true;
        }
        return false;
    }

    public FunctionEntity getMethod (String name) {
        for (FunctionEntity functionEntity: methods) {
            if (functionEntity.getName().equals(name))
                return functionEntity;
        }
        return null;
    }

    @Override
    public boolean equals(Type2 type2) {
        return true;
    }
}
