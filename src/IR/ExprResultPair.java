package IR;

import IR.Operand.IROper;

public class ExprResultPair {
    public IROper result;
    public IROper addr;

    public ExprResultPair(IROper result, IROper addr) {
        this.result = result;
        this.addr = addr;
    }

    public IROper getResult() {
        return result;
    }

    public IROper getAddr() {
        return addr;
    }
}
