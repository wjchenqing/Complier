package Codegen;

import Codegen.Operand.Addr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Stack {
    private Function function;

    private ArrayList<Addr> paramsAddrFromCallor = new ArrayList<>();
    private Map<Function, ArrayList<Addr>> paramsAddrForCallee = new HashMap<>();

    public Stack(Function function) {
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    public ArrayList<Addr> getParamsAddrFromCallor() {
        return paramsAddrFromCallor;
    }

    public Map<Function, ArrayList<Addr>> getParamsAddrForCallee() {
        return paramsAddrForCallee;
    }

    public void setParamsAddrFromCallor(ArrayList<Addr> paramsAddrFromCallor) {
        this.paramsAddrFromCallor = paramsAddrFromCallor;
    }

    public void setParamsAddrForCallee(Map<Function, ArrayList<Addr>> paramsAddrForCallee) {
        this.paramsAddrForCallee = paramsAddrForCallee;
    }

    public void addFormalParamAddr(Addr addr) {
        paramsAddrFromCallor.add(addr);
    }

    public void addAddrList(Function function, ArrayList<Addr> addrs) {
        paramsAddrForCallee.put(function, addrs);
    }

    public Addr getFormalParamAddr(int i) {
        return paramsAddrFromCallor.get(i);
    }

    public ArrayList<Addr> getAddrList(Function function) {
        return paramsAddrForCallee.get(function);
    }
}
