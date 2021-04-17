package IR.Instruction;

import IR.BasicBlock;
import IR.Function;
import IR.IRVisitor;
import IR.Operand.IROper;
import IR.Operand.NullConstant;
import IR.Operand.Register;
import IR.Type.IRType;
import IR.Type.PointerType;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Call extends IRInst {
    private Register result;
    private Function function;
    private ArrayList<IROper> params;

    public Call(BasicBlock currentBB, Register result, Function function, ArrayList<IROper> params) {
        super(currentBB);
        ArrayList<IRType> paramTypes = function.getFunctionType().getParamTypeList();
        if (!(params.size() == paramTypes.size())) {
            assert false;
        } else if ( (result != null) && !result.getType().equals(function.getFunctionType().getReturnType())) {
        } else {
            int bound = params.size();
            for (int i = 0; i < bound; i++) {
                IROper param = params.get(i);
                uses.add(param);
                param.addUse(this);
                if (param instanceof NullConstant) {
                    assert paramTypes.get(i) instanceof PointerType;
                } else {
//                    assert params.get(i).getType().equals(paramTypes.get(i));
                }
            }
        }
        this.result = result;
        this.function = function;
        this.params = params;
        if (result != null) {
            defs.add(result);
            result.addDef(this);
            currentBB.getCurrentFunction().defs.add(result);
        }
        function.calls.add(this);
    }

    @Override
    public void replaceUse(IROper o, IROper n) {
        int i = 0;
        for (IROper param: params) {
            if (param == o) {
                params.set(i, n);
                uses.remove(o);
                uses.add(n);
                n.addUse(this);
            }
            ++i;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        function.calls.remove(this);
    }

    @Override
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
        string.append(")");
        if (result != null) {
            return result.toString() + " = call " + function.getFunctionType().getReturnType().toString() + " " + function.toString()
                    + string;
        } else {
            return "call " + function.getFunctionType().getReturnType().toString() + " " + function.toString() + string;
        }
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
