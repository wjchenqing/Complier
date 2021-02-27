package IR.Instruction;

import IR.BasicBlock;
import IR.Function;
import IR.Operand.IROper;
import IR.Operand.NullConstant;
import IR.Operand.Register;
import IR.Type.IRType;
import IR.Type.PointerType;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Call extends IRInst {
    private Register result;
    private Function function;
    private ArrayList<IROper> params;

    public Call(BasicBlock currentBB, Register result, Function function, ArrayList<IROper> params) {
        super(currentBB);
        ArrayList<IRType> paramTypes = function.getFunctionType().getParamTypeList();
        if (!(params.size() == paramTypes.size()) || !result.getType().equals(function.getFunctionType().getReturnType())) {
            System.exit(-1);
        } else {
            int bound = params.size();
            for (int i = 0; i < bound; i++) {
                if (params.get(i) instanceof NullConstant) {
                    assert paramTypes.get(i) instanceof PointerType;
                } else {
                    assert params.get(i).getType().equals(paramTypes.get(i));
                }
            }
        }
        this.result = result;
        this.function = function;
        this.params = params;
    }

    public Register getResult() {
        return result;
    }

    public Function getFunction() {
        return function;
    }

    public ArrayList<IROper> getParams() {
        return params;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("(");
        int bound = params.size();
        AtomicInteger i = new AtomicInteger(1);
        for (IROper param: params) {
            string.append(param.getType().toString()).append(" ").append(param.toString());
            if (i.get() != bound) {
                string.append(", ");
            }
            i.incrementAndGet();
        }
        string.append(")\n");
        return result.toString() + " = call " + function.getFunctionType().getReturnType().toString() + " " + function.toString()
                + string;
    }
}
