package Codegen;

import Codegen.Operand.Addr;
import Codegen.Operand.RegisterVirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Stack {
    private Function function;

    int size;

    private final Map<RegisterVirtual, Addr> spillLocation = new HashMap<>();
    private ArrayList<Addr> paramsAddrFromCaller = new ArrayList<>();
    private Map<Function, ArrayList<Addr>> paramsAddrForCallee = new HashMap<>();

    public Stack(Function function) {
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    public ArrayList<Addr> getParamsAddrFromCaller() {
        return paramsAddrFromCaller;
    }

    public Map<Function, ArrayList<Addr>> getParamsAddrForCallee() {
        return paramsAddrForCallee;
    }

    public void putSpillLocation(RegisterVirtual rv, Addr addr) {
        assert addr.isStackLocation();
        System.out.println("put spill location: " + addr.getName());
        spillLocation.put(rv, addr);
    }

    public Map<RegisterVirtual, Addr> getSpillLocation() {
        return spillLocation;
    }

    public void setParamsAddrFromCaller(ArrayList<Addr> paramsAddrFromCaller) {
        this.paramsAddrFromCaller = paramsAddrFromCaller;
    }

    public void setParamsAddrForCallee(Map<Function, ArrayList<Addr>> paramsAddrForCallee) {
        this.paramsAddrForCallee = paramsAddrForCallee;
    }

    public void addFormalParamAddr(Addr addr) {
        paramsAddrFromCaller.add(addr);
    }

    public void addAddrList(Function function, ArrayList<Addr> addrs) {
        paramsAddrForCallee.put(function, addrs);
    }

    public Addr getFormalParamAddr(int i) {
        return paramsAddrFromCaller.get(i);
    }

    public ArrayList<Addr> getAddrList(Function function) {
        return paramsAddrForCallee.get(function);
    }

    public void setAndGetSize() {
        int spilledParamNum = 0;
        for (ArrayList<Addr> addr: paramsAddrForCallee.values()) {
            spilledParamNum = Math.max(spilledParamNum, addr.size());
        }
        size = spilledParamNum + spillLocation.size();

        for (int i = 0; i < paramsAddrFromCaller.size(); i++) {
            paramsAddrFromCaller.get(i).setOffset(4 * (size + i));
        }
        int cnt = 0;
        for (Addr addr: spillLocation.values()) {
            addr.setOffset(4 * (spilledParamNum + cnt));
            ++cnt;
        }
        for (ArrayList<Addr> addrs: paramsAddrForCallee.values()) {
            for (int i = 0; i < addrs.size(); ++i) {
                addrs.get(i).setOffset(4 * i);
            }
        }
    }

    public int getSize() {
        return size;
    }
}
