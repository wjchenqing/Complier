package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
import IR.Operand.IROper;
import IR.Operand.Register;
import IR.Type.IntegerType;

public class Br extends IRInst {
    private IROper cond;
    private BasicBlock thenBlock;
    private BasicBlock elseBlock;

    public Br(BasicBlock currentBB, IROper cond, BasicBlock thenBlock, BasicBlock elseBlock) {
        super(currentBB);
        if (cond != null && !cond.getType().equals(new IntegerType(1))) {
            assert false;
        }
        this.cond = cond;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
        if (cond != null) {
            uses.add(cond);
            cond.addUse(this);
        }
    }

    @Override
    public void replaceUse(IROper o, IROper n) {
        if (cond == o) {
            uses.remove(cond);
            uses.add(n);
            cond = n;
            n.addUse(this);
        }
    }

    public IROper getCond() {
        return cond;
    }

    public BasicBlock getThenBlock() {
        return thenBlock;
    }

    public BasicBlock getElseBlock() {
        return elseBlock;
    }

    @Override
    public void setPredecessorAndSuccessor() {
        currentBB.addSuccessor(thenBlock);
        thenBlock.addPredecessor(currentBB);

        if (cond != null) {
            currentBB.addSuccessor(elseBlock);
            elseBlock.addPredecessor(currentBB);
        }
    }

    @Override
    public String toString() {
        if (cond != null) {
            return "br i1 " + cond.toString() + ", label " + thenBlock.toString() + ", label " + elseBlock.toString();
        } else {
            return "br label " + thenBlock.toString();
        }
    }

    @Override
    public Register getResult() {
        return null;
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void replaceBBUse(BasicBlock o, BasicBlock n) {
        if (thenBlock == o) {
            thenBlock = n;
            currentBB.getSuccessor().remove(o);
            currentBB.getSuccessor().add(n);
            n.getPredecessor().remove(o);
            n.getPredecessor().add(currentBB);
        }
        if (elseBlock == o) {
            elseBlock = n;
            currentBB.getSuccessor().remove(o);
            currentBB.getSuccessor().add(n);
            n.getPredecessor().remove(o);
            n.getPredecessor().add(currentBB);
        }
    }
}
