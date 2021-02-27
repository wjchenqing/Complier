package IR;

import IR.Instruction.Br;
import IR.Instruction.IRInst;
import IR.Instruction.Ret;

import java.util.ArrayList;

public class BasicBlock {
    private String name = null;

    private Function currentFunction = null;

    private BasicBlock prevBB = null;
    private BasicBlock nextBB = null;

    private IRInst headInst = null;
    private IRInst tailInst = null;

    public BasicBlock(String name, Function currentFunction) {
        this.name = name;
        this.currentFunction = currentFunction;
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
    }

    public void addInstAtTail(IRInst irInst) {
        if (isEmpty()) {
            headInst = irInst;
            tailInst = irInst;
        } else if (!(tailInst instanceof Br) && !(tailInst instanceof Ret)) {
            irInst.setPrevInst(tailInst);
            tailInst.setNextInst(irInst);
            tailInst = irInst;
        } else {
            System.exit(-1);
        }
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
}
