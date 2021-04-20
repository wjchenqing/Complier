package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
import IR.Operand.BoolConstant;
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
            if (cond instanceof BoolConstant) {
                if (((BoolConstant) cond).getValue()) {
                    this.cond = null;
                    this.thenBlock = thenBlock;
                    this.elseBlock = null;
                } else {
                    this.cond = null;
                    this.thenBlock = elseBlock;
                    this.elseBlock = null;
                }
            } else {
                uses.add(cond);
                cond.addUse(this);
            }
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
        if (cond instanceof BoolConstant) {
            if (((BoolConstant) cond).getValue()) {
                currentBB.getSuccessor().remove(elseBlock);
                if (elseBlock.getPredecessor().size() == 1) {
                    elseBlock.delete();
                } else {
                    elseBlock.getPredecessor().remove(currentBB);
                    for (IRInst irInst = elseBlock.getHeadInst(); irInst instanceof Phi; irInst = irInst.getNextInst()) {
                        ((Phi) irInst).Check();
                    }
                }
                uses.remove(cond);
                cond.getUses().remove(this);
                cond = null;
                currentBB.getCurrentFunction().computeDFSListAgain = true;
                currentBB.getCurrentFunction().computePostDFSListAgain =true;
                currentBB.getCurrentFunction().computePostReverseDFSListAgain =true;
            } else {
                currentBB.getSuccessor().remove(thenBlock);
                thenBlock.getPredecessor().remove(currentBB);
                if (thenBlock.getPredecessor().isEmpty()) {
                    thenBlock.delete();
                } else {
                    for (IRInst irInst = thenBlock.getHeadInst(); irInst instanceof Phi; irInst = irInst.getNextInst()) {
                        ((Phi) irInst).Check();
                    }
                }
                uses.remove(cond);
                cond.getUses().remove(this);
                cond = null;
                thenBlock = elseBlock;
                currentBB.getCurrentFunction().computeDFSListAgain = true;
                currentBB.getCurrentFunction().computePostDFSListAgain =true;
                currentBB.getCurrentFunction().computePostReverseDFSListAgain =true;
            }
            elseBlock = null;
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
            n.getPredecessor().add(currentBB);
            o.getPredecessor().remove(currentBB);
        }
        if (elseBlock == o) {
            elseBlock = n;
            currentBB.getSuccessor().remove(o);
            currentBB.getSuccessor().add(n);
            n.getPredecessor().add(currentBB);
            o.getPredecessor().remove(currentBB);
        }
    }

    @Override
    public void deleteInst() {
        super.deleteInst();
        thenBlock.getPredecessor().remove(currentBB);
        currentBB.getSuccessor().remove(thenBlock);
        if (elseBlock != null) {
            elseBlock.getPredecessor().remove(currentBB);
            currentBB.getSuccessor().remove(elseBlock);
        }
    }
}
