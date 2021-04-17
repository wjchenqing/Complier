package IR;

import Frontend.Entity.FunctionEntity;
import Frontend.Entity.VariableEntity;
import Frontend.Scope.Scope;
import Frontend.Type.ClassType2;
import Frontend.Type.Type2;
import Frontend.Type.TypeTable;
import IR.Instruction.Alloca;
import IR.Instruction.Store;
import IR.Operand.*;
import IR.Type.*;

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
        function = new Function(this, "print", returnType, functionType, parameterList, false, true);
        functionMap.put(function.getName(), function);

        returnType = new VoidType();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str");
        paramTypeList = new ArrayList<>();
        paramTypeList.add(parameter.getType());
        parameterList = new ArrayList<>();
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function(this,"println", returnType, functionType, parameterList, false, true);
        functionMap.put(function.getName(), function);

        returnType = new VoidType();
        parameter = new Parameter(new IntegerType(32), "n");
        paramTypeList = new ArrayList<>();
        paramTypeList.add(parameter.getType());
        parameterList = new ArrayList<>();
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function(this,"printInt", returnType, functionType, parameterList, false, true);
        functionMap.put(function.getName(), function);

        returnType = new VoidType();
        parameter = new Parameter(new IntegerType(32), "n");
        paramTypeList = new ArrayList<>();
        paramTypeList.add(parameter.getType());
        parameterList = new ArrayList<>();
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function(this,"printlnInt", returnType, functionType, parameterList, false, true);
        functionMap.put(function.getName(), function);


        returnType = new PointerType(new IntegerType(8));
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function(this,"getString", returnType, functionType, parameterList, false, true);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(32);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function(this,"getInt", returnType, functionType, parameterList, false, true);
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
        function = new Function(this, "_string_less", returnType, functionType, parameterList, false, false);
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
        function = new Function(this, "_string_greater", returnType, functionType, parameterList, false, false);
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
        function = new Function(this, "_string_lessEqual", returnType, functionType, parameterList, false, false);
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
        function = new Function(this, "_string_greaterEqual", returnType, functionType, parameterList, false, false);
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
        function = new Function(this, "_string_equal", returnType, functionType, parameterList, false, false);
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
        function = new Function(this, "_string_notequal",returnType, functionType, parameterList, false, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(32);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "string");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function(this, "_string_length", returnType, functionType, parameterList, false, false);
        functionMap.put(function.getName(), function);

        returnType = new PointerType(new IntegerType(8));
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new IntegerType(32), "n");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function(this, "toString", returnType, functionType, parameterList, false, false);
        functionMap.put(function.getName(), function);

        returnType = new PointerType(new IntegerType(8));
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new IntegerType(32), "left");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        parameter = new Parameter(new IntegerType(32), "right");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function(this, "_string_substring", returnType, functionType, parameterList, false, false);
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
        function = new Function(this, "_string_ord", returnType, functionType, parameterList, false, false);
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
        function = new Function(this, "_string_concatenate", returnType, functionType, parameterList, false, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(32);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "array");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function(this, "_array_size", returnType, functionType, parameterList, false, false);
        functionMap.put(function.getName(), function);

        returnType = new IntegerType(32);
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new PointerType(new IntegerType(8)), "str");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function(this, "_string_parseInt", returnType, functionType, parameterList, false, false);
        functionMap.put(function.getName(), function);

        returnType = new PointerType(new IntegerType(8));
        paramTypeList = new ArrayList<>();
        parameterList = new ArrayList<>();
        parameter = new Parameter(new IntegerType(32), "size");
        paramTypeList.add(parameter.getType());
        parameterList.add(parameter);
        functionType = new FunctionType(returnType, paramTypeList);
        function = new Function(this, "malloc", returnType, functionType, parameterList, false, false);
        functionMap.put(function.getName(), function);

    }

    public int getStringConstMapSize() {
        return StringConstMap.size();
    }

    public void addStringConst(GlobalVariable stringConst) {
        StringConstMap.put(stringConst.getName(), stringConst);
    }

    public Map<String, GlobalVariable> getStringConstMap() {
        return StringConstMap;
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
        if (returnType instanceof StructureType)
            returnType = new PointerType(returnType);
        if (returnType == null) {
            returnType = new VoidType();
        }
        ArrayList<IRType> paramTypeList = new ArrayList<>();

        if (functionScope.inClassScope()) {
            String className = functionScope.getClassType().getTypeName();
            identifier = className + "." + identifier;

            newFunction = new Function(this, identifier, returnType, null, null, true, true);
            currentBB = newFunction.getEntranceBB();
            IRType tmp = irTypeTable.get(functionScope.getClassType());
            if (tmp instanceof StructureType) {
                tmp = new PointerType(tmp);
            }

//            Parameter parameter = new Parameter(tmp, "this");
//            paramTypeList.add(parameter.getType());
//            parameters.add(parameter);
//            newFunction.CheckAndSetName(parameter.getName(), parameter);
//            Register addr = new Register(new PointerType(parameter.getType()), className + ".this");
//            ArrayList<IROper> paramsForMalloc = new ArrayList<>();
//            paramsForMalloc.add(new IntegerConstant(new PointerType(parameter.getType()).getByte()));
//            Function mallocFunc = functionMap.get("malloc");
//            Register mallocAddr = new Register(new PointerType(new IntegerType(8)), "mallocAddrForParam");
//            newFunction.CheckAndSetName(mallocAddr.getName(), mallocAddr);
//            currentBB.addInstAtTail(new Call(currentBB, addr, mallocFunc, paramsForMalloc));
//            currentBB.addInstAtTail(new BitCastTo(currentBB, addr, mallocAddr, new PointerType(parameter.getType())));
//            currentBB.addInstAtTail(new Store(currentBB, parameter, addr));
            Parameter parameter = new Parameter(tmp, "this");
            paramTypeList.add(parameter.getType());
            parameters.add(parameter);
            newFunction.CheckAndSetName(parameter.getName(), parameter);
            Register addr = new Register(new PointerType(parameter.getType()), className + ".this");
//            ArrayList<IROper> paramsForMalloc = new ArrayList<>();
//            paramsForMalloc.add(new IntegerConstant(new PointerType(parameter.getType()).getByte()));
//            Function mallocFunc = functionMap.get("malloc");
//            Register mallocAddr = new Register(new PointerType(new IntegerType(8)), "mallocAddrForParam");
//            newFunction.CheckAndSetName(mallocAddr.getName(), mallocAddr);
//            currentBB.addInstAtTail(new Call(currentBB, mallocAddr, mallocFunc, paramsForMalloc));
//            currentBB.addInstAtTail(new BitCastTo(currentBB, addr, mallocAddr, new PointerType(parameter.getType())));
//            currentBB.addInstAtTail(new Store(currentBB, parameter, addr));
            currentBB.addInstAtTail(new Alloca(currentBB, addr, parameter.getType()));
            currentBB.addInstAtTail(new Store(currentBB, parameter, addr));
            newFunction.CheckAndSetName(addr.getName(), addr);
        } else {
//            System.err.println(identifier);
            newFunction = new Function(this, identifier, returnType, null, null, true, true);
            currentBB = newFunction.getEntranceBB();
        }

        ArrayList<VariableEntity> variableEntities = functionEntity.getParams();
        for (VariableEntity variableEntity : variableEntities) {
            IRType tmp = irTypeTable.get(astTypeTable.getType2(variableEntity.getType()));
            if (tmp instanceof StructureType) {
                tmp = new PointerType(tmp);
            }
//            Parameter parameter = new Parameter(tmp, variableEntity.getName());
//            paramTypeList.add(parameter.getType());
//            parameters.add(parameter);
//            newFunction.CheckAndSetName(parameter.getName(), parameter);
//            Register addr = new Register(new PointerType(parameter.getType()), identifier + "." +variableEntity.getName());
//            ArrayList<IROper> paramsForMalloc = new ArrayList<>();
//            paramsForMalloc.add(new IntegerConstant(new PointerType(parameter.getType()).getByte()));
//            Function mallocFunc = functionMap.get("malloc");
//            Register mallocAddr = new Register(new PointerType(new IntegerType(8)), "mallocAddrForParam");
//            newFunction.CheckAndSetName(mallocAddr.getName(), mallocAddr);
//            currentBB.addInstAtTail(new Call(currentBB, addr, mallocFunc, paramsForMalloc));
//            currentBB.addInstAtTail(new BitCastTo(currentBB, addr, mallocAddr, new PointerType(parameter.getType())));
//            currentBB.addInstAtTail(new Store(currentBB, parameter, addr));
            Parameter parameter = new Parameter(tmp, variableEntity.getName());
            paramTypeList.add(parameter.getType());
            parameters.add(parameter);
            newFunction.CheckAndSetName(parameter.getName(), parameter);
            Register addr = new Register(new PointerType(parameter.getType()), identifier + "." +variableEntity.getName());
//            ArrayList<IROper> paramsForMalloc = new ArrayList<>();
//            paramsForMalloc.add(new IntegerConstant(new PointerType(parameter.getType()).getByte()));
//            Function mallocFunc = functionMap.get("malloc");
//            Register mallocAddr = new Register(new PointerType(new IntegerType(8)), "mallocAddrForParam");
//            newFunction.CheckAndSetName(mallocAddr.getName(), mallocAddr);
//            currentBB.addInstAtTail(new Call(currentBB, mallocAddr, mallocFunc, paramsForMalloc));
//            currentBB.addInstAtTail(new BitCastTo(currentBB, addr, mallocAddr, new PointerType(parameter.getType())));
//            currentBB.addInstAtTail(new Store(currentBB, parameter, addr));
            currentBB.addInstAtTail(new Alloca(currentBB, addr, parameter.getType()));
            currentBB.addInstAtTail(new Store(currentBB, parameter, addr));
            newFunction.CheckAndSetName(addr.getName(), addr);
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

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
