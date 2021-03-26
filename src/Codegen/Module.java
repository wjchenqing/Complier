package Codegen;

import Codegen.Operand.GlobalVar;

import java.util.HashMap;
import java.util.Map;

public class Module {
    private final Map<String, Function> functionMap = new HashMap<>();
    private final Map<String, GlobalVar> globalVarMap = new HashMap<>();

    public Function getFunction(String name) {
        return functionMap.get(name);
    }

    public int getFunctionMapSize() {
        return functionMap.size();
    }

    public GlobalVar getGlobalVar(String name) {
        return globalVarMap.get(name);
    }

    public int getGlobalVarMapSize() {
        return globalVarMap.size();
    }

    public void addFunction(Function function) {
        functionMap.put(function.getName(), function);
    }

    public void addGlobalVar(GlobalVar globalVar) {
        globalVarMap.put(globalVar.getIdentifier(), globalVar);
    }

    public Map<String, Function> getFunctionMap() {
        return functionMap;
    }

    public Map<String, GlobalVar> getGlobalVarMap() {
        return globalVarMap;
    }

    public void accept(CodegenVisitor visitor) {
        visitor.visit(this);
    }
}
