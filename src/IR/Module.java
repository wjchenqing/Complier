package IR;

import Frontend.Entity.FunctionEntity;
import Frontend.Entity.VariableEntity;
import Frontend.Scope.Scope;
import Frontend.Type.ClassType2;
import Frontend.Type.Type2;
import Frontend.Type.TypeTable;
import IR.Instruction.Alloca;
import IR.Instruction.Store;
import IR.Operand.GlobalVariable;
import IR.Operand.Parameter;
import IR.Operand.Register;
import IR.Type.*;
import jdk.nashorn.internal.ir.FunctionNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Module {
    private final Map<String, Function> functionMap = new HashMap<>();
    private final Map<String, GlobalVariable> globalVariableMap = new HashMap<>();
    private final Map<String, StructureType> structureTypeMap = new HashMap<>();
    private final Map<String, GlobalVariable> StringConstMap = new HashMap<>();
    private final TypeTable astTypeTable;
    private final IRTypeTable irTypeTable;

    public Module(TypeTable astTypeTable) {
        this.astTypeTable = astTypeTable;
        irTypeTable = new IRTypeTable(astTypeTable);

        for (Type2 astType: irTypeTable.getTypeTable().keySet()) {
            if (astType instanceof ClassType2) {
                assert irTypeTable.get(astType) instanceof StructureType;
                structureTypeMap.put(astType.getTypeName(), (StructureType) irTypeTable.get(astType));
            }
        }

        Function function;
        FunctionType functionType;
        IRType returnType;
        ArrayList<IRType> paramTypeList;
        ArrayList<Parameter> parameterList;
        Parameter parameter;

        returnType = new VoidType();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str");
        paramTypeList = new ArrayList<>();
        paramTypeList.add(parameter.getType());
        parameterList = new ArrayList<>();
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("print", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new VoidType();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str");
        paramTypeList = new ArrayList<>();
        paramTypeList.add(parameter.getType());
        parameterList = new ArrayList<>();
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("println", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new VoidType();
        parameter = new Parameter(new IntegerType(32), "n");
        paramTypeList = new ArrayList<>();
        paramTypeList.add(parameter.getType());
        parameterList = new ArrayList<>();
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("printInt", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new VoidType();
        parameter = new Parameter(new IntegerType(32), "n");
        paramTypeList = new ArrayList<>();
        paramTypeList.add(parameter.getType());
        parameterList = new ArrayList<>();
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("printlnInt", functionType, parameterList, false);
        functionMap.put(function.getName(), function);


        returnType = new PointerType(new IntegerType(8));
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("getString", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(32);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("getInt", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(1);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str1");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str2");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_string_less", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(1);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str1");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str2");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_string_greater", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(1);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str1");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str2");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_string_lessEqual", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(1);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str1");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str2");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_string_greaterEqual", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(1);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str1");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str2");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_string_equal", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(1);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str1");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str2");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_string_notequal", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(32);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "string");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_string_length", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new PointerType(new IntegerType(8));
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new IntegerType(32), "n");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("toString", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new PointerType(new IntegerType(8));
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new IntegerType(8), "left");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new IntegerType(8), "right");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_string_substring", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(32);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new IntegerType(32), "p");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_string_ord", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new PointerType(new IntegerType(8));
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str1");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str2");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_string_concatenate", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(32);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "array");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_array_size", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(32);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("_string_parseInt", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

        returnType = new PointerType(new IntegerType(8));
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new IntegerType(32), "size");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function("malloc", functionType, parameterList, false);
        functionMap.put(function.getName(), function);

    }

    public int getStringConstMapSize() {
        return StringConstMap.size();
    }

    public void addStringConst(GlobalVariable stringConst) {
        StringConstMap.put(stringConst.getName(), stringConst);
    }

    public GlobalVariable getStringConst(String name) {
        return StringConstMap.get(name);
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

    public Function getFunction(String identifier) {
        return functionMap.get(identifier);
    }

    public void addFunction(Function function) {
        functionMap.put(function.getName(), function);
    }

    public void addFunction(AST.Function function) {
        Scope functionScope = function.getScope();
        String identifier = function.getIdentifier();
        ArrayList<Parameter> parameters = new ArrayList<>();
        FunctionEntity functionEntity = functionScope.getAnyFunctionEntity(identifier);

        Function newFunction;
        BasicBlock currentBB;

        IRType returnType = irTypeTable.get(astTypeTable.getType2(functionEntity.getReturnType()));
        ArrayList<IRType> paramTypeList = new ArrayList<>();

        if (functionScope.inClassScope()) {
            String className = functionScope.getClassType().getTypeName();
            identifier = className + "." + identifier;

            newFunction = new Function(identifier, null, null, true);
            currentBB = newFunction.getHeadBB();

            Parameter parameter = new Parameter(irTypeTable.get(functionScope.getClassType()), "this");
            paramTypeList.add(parameter.getType());
            parameters.add(parameter);
            Register addr = new Register(new PointerType(parameter.getType()), className + ".this");
            currentBB.addInstAtTail(new Alloca(currentBB, addr, parameter.getType()));
            currentBB.addInstAtTail(new Store(currentBB, parameter, addr));
            newFunction.addOperand(addr.getName(), addr);
        } else {
            newFunction = new Function(identifier, null, null, true);
            currentBB = newFunction.getHeadBB();
        }

        ArrayList<VariableEntity> variableEntities = functionEntity.getParams();
        for (VariableEntity variableEntity : variableEntities) {
            Parameter parameter = new Parameter(irTypeTable.get(astTypeTable.getType2(variableEntity.getType())),
                    variableEntity.getName());
            paramTypeList.add(parameter.getType());
            parameters.add(parameter);
            Register addr = new Register(new PointerType(parameter.getType()), identifier + "." +variableEntity.getName());
            currentBB.addInstAtTail(new Alloca(currentBB, addr, parameter.getType()));
            currentBB.addInstAtTail(new Store(currentBB, parameter, addr));
            newFunction.addOperand(addr.getName(), addr);
            variableEntity.setAddr(addr);
        }

        newFunction.setParameters(parameters);
        newFunction.setFunctionType(new FunctionType(returnType, paramTypeList));
        newFunction.CheckParameterType();

        addFunction(newFunction);
    }

    public void addGlobalVariable(GlobalVariable globalVariable) {
        globalVariableMap.put(globalVariable.getName(), globalVariable);
    }
}