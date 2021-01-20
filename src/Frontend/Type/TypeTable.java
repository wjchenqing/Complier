package Frontend.Type;

import AST.ArrayType;
import AST.PrimitiveType;
import AST.TypeNode;

import java.util.HashMap;
import java.util.Map;

public class TypeTable {
    private Map<TypeNode, Type2> typeTable = new HashMap<>();

    public TypeTable() {
        typeTable.put(new PrimitiveType("int"), new IntType2());
        typeTable.put(new PrimitiveType("bool"), new BoolType2());
        typeTable.put(new PrimitiveType("string"), new StringType2());
        typeTable.put(new PrimitiveType("void"), new VoidType2());
    }

    public Map<TypeNode, Type2> getTypeTable() {
        return typeTable;
    }

    public void put(TypeNode typeNode, Type2 type2) {
        if (typeTable.containsKey(typeNode)) {
            System.exit(-1);
        } else {
            typeTable.put(typeNode, type2);
        }
    }

    public Type2 getType2(TypeNode typeNode) {
        if (typeNode instanceof ArrayType) {
            TypeNode baseType = ((ArrayType) typeNode).getBaseType();
            int dim = ((ArrayType) typeNode).getDim();
            return new ArrayType2(typeTable.get(baseType), dim);
        } else {
            return typeTable.get(typeNode);
        }
    }

    public boolean contains(TypeNode typeNode) {
        return typeTable.containsKey(typeNode);
    }
}
