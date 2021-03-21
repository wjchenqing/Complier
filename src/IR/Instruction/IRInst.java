package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
import IR.Operand.Register;

abstract public class IRInst {
    protected BasicBlock currentBB = null;
    protected IRInst prevInst = null;
    protected IRInst nextInst = null;

    public IRInst(BasicBlock currentBB) {
        this.currentBB = currentBB;
    }

    public void setCurrentBB(BasicBlock currentBB) {
        this.currentBB = currentBB;
    }

    public void setPrevInst(IRInst prevInst) {
        this.prevInst = prevInst;
    }

    public void setNextInst(IRInst nextInst) {
        this.nextInst = nextInst;
    }

    public BasicBlock getCurrentBB() {
        return currentBB;
    }

    public IRInst getPrevInst() {
        return prevInst;
    }

    public IRInst getNextInst() {
        return nextInst;
    }

    public void setPredecessorAndSuccessor() {}

    @Override
    abstract public String toString();

    abstract public Register getResult();

    abstract public void accept(IRVisitor visitor);
}