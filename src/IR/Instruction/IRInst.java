package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
import IR.Operand.IROper;
import IR.Operand.Register;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

abstract public class IRInst {
    protected BasicBlock currentBB = null;
    protected IRInst prevInst = null;
    protected IRInst nextInst = null;

    public boolean deleted = false;

    protected Set<IROper> uses = new HashSet<>();
    protected Set<IROper> defs = new HashSet<>();

//    protected Set<BasicBlock> useBB = new HashSet<>();

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

    public void addInstNext(IRInst instruction) {
        instruction.setPrevInst(this);
        instruction.setNextInst(nextInst);
        if (nextInst != null) {
            nextInst.setPrevInst(instruction);
        } else {
            currentBB.setTailInst(instruction);
        }
        nextInst = instruction;
    }

    public void addInstPrev(IRInst instruction) {
        instruction.setPrevInst(prevInst);
        instruction.setNextInst(this);
        if (prevInst != null) {
            prevInst.setNextInst(instruction);
        } else {
            currentBB.setHeadInst(instruction);
        }
        prevInst = instruction;
    }

    public Set<IROper> getUses() {
        return uses;
    }

    public Set<IROper> getDefs() {
        return defs;
    }

    public void deleteInst() {
        if ((prevInst == null) && (nextInst == null)) {
            if (!(this instanceof Br)) {
                return;
            }
            Set<BasicBlock> predecessor = new HashSet<>(currentBB.getPredecessor());
            Set<BasicBlock> successor = new HashSet<>(currentBB.getSuccessor());
            predecessor.removeAll(currentBB.getSuccessor());
            successor.removeAll(currentBB.getPredecessor());
            currentBB.getSuccessor().clear();
            currentBB.getSuccessor().addAll(successor);
            for (BasicBlock s: currentBB.getSuccessor()) {
                if (s.DominanceFrontier.contains(currentBB)) {
                    successor.remove(s);
                }
            }
            if (successor.size() == 1) {
                BasicBlock s = successor.iterator().next();
                for (BasicBlock p: predecessor) {
                    assert p.getTailInst() instanceof Br;
                    p.getTailInst().replaceBBUse(currentBB, s);
                }
            } else if (successor.size() == 0) {

            } else {
                assert predecessor.size() == 0;
            }
            currentBB.delete();
            currentBB.getCurrentFunction().computeDFSListAgain = true;
            currentBB.getCurrentFunction().computePostDFSListAgain = true;
            currentBB.getCurrentFunction().computePostReverseDFSListAgain = true;
        } else if (prevInst == null) {
            currentBB.setHeadInst(nextInst);
            nextInst.setPrevInst(null);
        } else if (nextInst == null) {
            currentBB.setTailInst(prevInst);
            prevInst.setNextInst(null);
        } else {
            prevInst.setNextInst(nextInst);
            nextInst.setPrevInst(prevInst);
        }
        destroy();
        deleted = true;
    }

    public void completelyDelete() {
        deleteInst();
        if (getResult() != null) {
            while (!getResult().getUses().isEmpty()) {
                Set<IRInst> uses = new LinkedHashSet<>(getResult().getUses());
                for (IRInst use : uses) {
                    use.completelyDelete();
                }
            }
        }
    }

    public void destroy() {
        for (IROper irOper: uses) {
            irOper.getUses().remove(this);
        }
        Set<Register> funcDefs = currentBB.getCurrentFunction().defs;
        for (IROper irOper: defs) {
            irOper.getDefs().remove(this);
            if (irOper instanceof Register) {
                funcDefs.remove(irOper);
            }
        }
//        prevInst = null;
//        nextInst = null;
    }

    public void replaceBBUse(BasicBlock o, BasicBlock n) {

    }

    abstract public void replaceUse(IROper o, IROper n);
}