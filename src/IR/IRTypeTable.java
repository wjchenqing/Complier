package IR;

import Frontend.Entity.VariableEntity;
import Frontend.Type.*;
import IR.Type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IRTypeTable {
    private Map<Type2, IRType> typeTable = new HashMap<>();

    public IRTypeTable(TypeTable astTypeTable) {
        for (Type2 astType: astTypeTable.getTypeTable().values()) {
            if (astType instanceof BoolType2) {
                typeTable.put(astType, new IntegerType(1));
            } else if (astType instanceof IntType2) {
                typeTable.put(astType, new IntegerType(32));
            } else if (astType instanceof VoidType2) {
                typeTable.put(astType, new VoidType());
            } else if (astType instanceof StringType2) {
                typeTable.put(astType, new PointerType(new IntegerType(8)));
            } else if (astType instanceof ClassType2) {
                typeTable.put(astType, new StructureType(astType.getTypeName()));
            } else {
                System.exit(-1);
            }
        }

        for (Type2 astType: typeTable.keySet()) {
            if (astType instanceof ClassType2) {
                for (VariableEntity member: ((ClassType2) astType).getMembers()) {
                    ((StructureType) typeTable.get(astType)).putIRType(typeTable.get(astTypeTable.getType2(member.getType())));
                }

            }
        }
    }

    public Map<Type2, IRType> getTypeTable() {
        return typeTable;
    }

    public IRType get(Type2 type2) {
        return typeTable.get(type2);
    }
}
