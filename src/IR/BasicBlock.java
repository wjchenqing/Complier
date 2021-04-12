package IR;

import IR.Instruction.Br;
import IR.Instruction.IRInst;
import IR.Instruction.Ret;
import IR.Operand.IROper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class BasicBlock {
    private String name = null;

    private Function currentFunction = null;

    private BasicBlock prevBB = null;
    private BasicBlock nextBB = null;

    private final Set<BasicBlock> predecessor = new LinkedHashSet<>();
    private final Set<BasicBlock> successor = new LinkedHashSet<>();

    private IRInst headInst = null;
    private IRInst tailInst = null;

    public BasicBlock dom = null;
    public int dfsNum;
    public int postDfsNum = 0;
    public Set<BasicBlock> DominanceFrontier = new HashSet<>();
    public Set<BasicBlock> DomChildren = new HashSet<>();

    public int depth;

    public BasicBlock(String name, Function currentFunction, int depth) {
        this.depth = depth;
        this.name = name;
        this.currentFunction = currentFunction;
    }

    public void addPredecessor(BasicBlock basicBlock) {
        predecessor.add(basicBlock);
    }

    public Set<BasicBlock> getPredecessor() {
        return predecessor;
    }

    public Set<BasicBlock> getSuccessor() {
        return successor;
    }

    public void addSuccessor(BasicBlock basicBlock) {
        successor.add(basicBlock);
    }

    public boolean isEmpty() {
        return (headInst == null) && (tailInst == null);
    }

    public void addInstAtHead(IRInst irInst) {
        if (isEmpty()) {
            headInst = irInst;
            tailInst = irInst;
        } else {
            headInst.setPrevInst(irInst);
            irInst.setNextInst(headInst);
            headInst = irInst;
        }
        irInst.setPredecessorAndSuccessor();
    }

    public void addInstAtTail(IRInst irInst) {
        if (isEmpty()) {
            headInst = irInst;
            tailInst = irInst;
        } else if (!(tailInst instanceof Br) && !(tailInst instanceof Ret)) {
            irInst.setPrevInst(tailInst);
            tailInst.setNextInst(irInst);
            tailInst = irInst;
        } else if (tailInst instanceof Br) {
            return;
        } else {
            assert false;
        }
        irInst.setPredecessorAndSuccessor();
    }

    public ArrayList<IRInst> getInstList() {
        ArrayList<IRInst> instList = new ArrayList<>();
        for (IRInst cur = headInst; cur != null; cur = cur.getNextInst()) {
            instList.add(cur);
        }
        return instList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentFunction(Function currentFunction) {
        this.currentFunction = currentFunction;
    }

    public void setPrevBB(BasicBlock prevBB) {
        this.prevBB = prevBB;
    }

    public void setNextBB(BasicBlock nextBB) {
        this.nextBB = nextBB;
    }

    public void setHeadInst(IRInst headInst) {
        this.headInst = headInst;
    }

    public void setTailInst(IRInst tailInst) {
        this.tailInst = tailInst;
    }

    public String getName() {
        return name;
    }

    public Function getCurrentFunction() {
        return currentFunction;
    }

    public BasicBlock getPrevBB() {
        return prevBB;
    }

    public BasicBlock getNextBB() {
        return nextBB;
    }

    public IRInst getHeadInst() {
        return headInst;
    }

    public IRInst getTailInst() {
        return tailInst;
    }

    @Override
    public String toString() {
        return "%" + name;
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }

    public void delete() {
        if (currentFunction.getHeadBB() == this) {
            if (nextBB != null) {
                currentFunction.setHeadBB(this.nextBB);
                nextBB.setPrevBB(null);
            } else {
                currentFunction.setHeadBB(currentFunction.getReturnBB());
                currentFunction.setTailBB(currentFunction.getReturnBB());
                currentFunction.setReturnBB(null);
            }
        } else if (currentFunction.getTailBB() == this) {
            currentFunction.setTailBB(this.prevBB);
            prevBB.setNextBB(null);
        } else if ((nextBB == null)) {
            currentFunction.setReturnBB(null);
        } else {
            if (prevBB == null) {
                assert false;
            }
            prevBB.setNextBB(nextBB);
            nextBB.setPrevBB(prevBB);
        }
        for (BasicBlock basicBlock: predecessor) {
            basicBlock.getSuccessor().remove(this);
        }
        for (BasicBlock basicBlock: successor) {
            basicBlock.getPredecessor().remove(this);
        }
        for (IRInst cur = headInst; cur != null; cur = cur.getNextInst()) {
            cur.destroy();
        }
    }

    public void mergeWithSuccessor(BasicBlock basicBlock) {
        tailInst.deleteInst();
        tailInst.setNextInst(basicBlock.headInst);
        basicBlock.headInst.setPrevInst(tailInst);
        tailInst = basicBlock.tailInst;

        for (IRInst irInst = basicBlock.headInst; irInst != null; irInst = irInst.getNextInst()) {
            irInst.setCurrentBB(this);
        }

        if (currentFunction.getTailBB() == basicBlock) {
            currentFunction.setTailBB(basicBlock.prevBB);
            basicBlock.prevBB.setNextBB(null);
        } else if (basicBlock.getNextBB() == null) {
            currentFunction.setReturnBB(null);
        } else if (currentFunction.getHeadBB() == basicBlock) {
            currentFunction.setHeadBB(basicBlock.nextBB);
            basicBlock.nextBB.setPrevBB(null);
        } else {
            basicBlock.prevBB.setNextBB(basicBlock.nextBB);
            basicBlock.nextBB.setPrevBB(basicBlock.prevBB);
        }
        successor.remove(basicBlock);
        for (BasicBlock bb: basicBlock.successor) {
            bb.getPredecessor().remove(basicBlock);
            bb.getPredecessor().add(this);
            successor.add(bb);
        }
    }
}
