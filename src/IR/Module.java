package IR;

import Frontend.Entity.VariableEntity;
import Frontend.Type.ClassType2;
import Frontend.Type.Type2;
import Frontend.Type.TypeTable;
import IR.Operand.GlobalVariable;
import IR.Type.IRType;
import IR.Type.StructureType;

import java.util.HashMap;
import java.util.Map;

public class Module {
    private final Map<String, Function> functionMap = new HashMap<>();
    private final Map<String, GlobalVariable> globalVariableMap = new HashMap<>();
    private final Map<String, StructureType> structureTypeMap = new HashMap<>();
    private final IRTypeTable irTypeTable;

    public Module(TypeTable astTypeTable) {
        irTypeTable = new IRTypeTable(astTypeTable);

        for (Type2 astType: irTypeTable.getTypeTable().keySet()) {
            if (astType instanceof ClassType2) {
                assert irTypeTable.get(astType) instanceof StructureType;
                structureTypeMap.put(astType.getTypeName(), (StructureType) irTypeTable.get(astType));
            }
        }
    }

    public Map<String, Function> getFunctionMap() {
        return functionMap;
    }

    public Map<String, GlobalVariable> getGlobalVariableMap() {
        return globalVariableMap;
    }

    public Map<String, StructureType> getStructureTypeMap() {
        return structureTypeMap;
    }

    public IRTypeTable getIrTypeTable() {
        return irTypeTable;
    }

    public void addFunction(Function function) {
        functionMap.put(function.getName(), function);
    }

    public void addGlobalVariable(GlobalVariable globalVariable) {
        globalVariableMap.put(globalVariable.getName(), globalVariable);
    }
}
