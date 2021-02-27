package IR;

import IR.Operand.Parameter;
import IR.Operand.Register;
import IR.Type.FunctionType;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Function {
    private String name;
    private FunctionType functionType;
    private ArrayList<Parameter> parameters;
    private BasicBlock headBB = null;
    private BasicBlock tailBB = null;
    private BasicBlock returnBB = null;
    private Register returnValue = null;

    public Function(String name, FunctionType functionType, ArrayList<Parameter> parameters) {
        if (!(parameters.size() == functionType.getParamTypeList().size() &&
                IntStream.range(0, parameters.size()).allMatch(i ->
                        (parameters.get(i).getType().equals(functionType.getParamTypeList().get(i)))) &&
                functionType.getReturnType().equals(returnValue.getType()))) {
            System.exit(-1);
        }
        this.name = name;
        this.functionType = functionType;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public BasicBlock getHeadBB() {
        return headBB;
    }

    public BasicBlock getTailBB() {
        return tailBB;
    }

    public BasicBlock getReturnBB() {
        return returnBB;
    }

    public Register getReturnValue() {
        return returnValue;
    }

    public void setHeadBB(BasicBlock headBB) {
        this.headBB = headBB;
    }

    public void setTailBB(BasicBlock tailBB) {
        this.tailBB = tailBB;
    }

    public void setReturnBB(BasicBlock returnBB) {
        this.returnBB = returnBB;
    }

    public void setReturnValue(Register returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
