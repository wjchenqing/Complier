package Frontend.Type;

import AST.ArrayType;
import AST.Function;
import AST.PrimitiveType;
import Frontend.Entity.FunctionEntity;
import Frontend.Entity.VariableEntity;

import java.util.ArrayList;

public class ArrayType2 extends Type2 {
    private Type2 baseType;
    private int dim;
    private ArrayList<FunctionEntity> builtInMethods;

    public ArrayType2(Type2 baseType2, int dim2) {
        typeName = baseType2.typeName;
        baseType = baseType2;
        dim = dim2;
        builtInMethods = new ArrayList<>();
        ArrayList<VariableEntity> params = new ArrayList<>();
        builtInMethods.add(new FunctionEntity("size", new PrimitiveType("int"), params, null));
    }

    public Type2 getBaseType() {
        return baseType;
    }

    public int getDim() {
        return dim;
    }

    @Override
    public boolean equals(Type2 type2) {
        return ((type2 instanceof ArrayType2) && (baseType == ((ArrayType2) type2).baseType) && (dim == ((ArrayType2) type2).dim));
    }
}
