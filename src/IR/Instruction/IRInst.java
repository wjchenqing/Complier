package IR.Instruction;

import IR.BasicBlock;

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

    @Override
    abstract public String toString();
}