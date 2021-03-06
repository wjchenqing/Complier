package IR;

import IR.Instruction.Br;
import IR.Instruction.IRInst;
import IR.Instruction.Phi;
import IR.Instruction.Ret;
import IR.Operand.IROper;
import Util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class BasicBlock {
    private String name;

    private Function currentFunction;

//    private BasicBlock prevBB = null;
//    private BasicBlock nextBB = null;

    private final Set<BasicBlock> predecessor = new LinkedHashSet<>();
    private final Set<BasicBlock> successor = new LinkedHashSet<>();

    private IRInst headInst = null;
    private IRInst tailInst = null;

    public BasicBlock dom = null;
    public int dfsNum;
    public int postDfsNum = 0;
    public Set<BasicBlock> DominanceFrontier = new LinkedHashSet<>();

    public BasicBlock reverseDom = null;
    public int postReverseDFSNum = 0;
    public Set<BasicBlock> reverseDominanceFrontier = new LinkedHashSet<>();

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

//    public void setPrevBB(BasicBlock prevBB) {
//        this.prevBB = prevBB;
//    }
//
//    public void setNextBB(BasicBlock nextBB) {
//        this.nextBB = nextBB;
//    }

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

//    public BasicBlock getPrevBB() {
//        return prevBB;
//    }

//    public BasicBlock getNextBB() {
//        return nextBB;
//    }

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
        if (currentFunction.getEntranceBB() == this) {
            if (!(successor.size() == 1)) {
                assert false;
            }
            currentFunction.setEntranceBB(successor.iterator().next());
            currentFunction.getBlockSet().remove(this);
        } else if (currentFunction.getReturnBB() == this) {
            if (!(predecessor.size() == 1)) {
                assert false;
            }
            currentFunction.setReturnBB(predecessor.iterator().next());
            currentFunction.getBlockSet().remove(this);
        } else {
            currentFunction.getBlockSet().remove(this);
        }
        for (BasicBlock basicBlock: predecessor) {
            basicBlock.getSuccessor().remove(this);
        }
        for (BasicBlock s: successor) {
            for (IRInst phi = s.getHeadInst(); phi instanceof Phi; phi = phi.getNextInst()) {
                Set<Pair<BasicBlock, IROper>> list = new LinkedHashSet<>(((Phi) phi).getPossiblePredecessorSet());
                for (Pair<BasicBlock, IROper> pair: list) {
                    if (pair.getFirst() == this) {
                        ((Phi) phi).getPossiblePredecessorSet().remove(pair);
                        phi.getUses().remove(pair.getSecond());
                        pair.getSecond().getUses().remove(phi);
                    }
                }
                if (((Phi) phi).getPossiblePredecessorSet().size() == 1) {
                    IROper n = ((Phi) phi).getPossiblePredecessorSet().iterator().next().getSecond();
                    IROper o = phi.getResult();
                    Set<IRInst> visited = new LinkedHashSet<>();
                    while (!o.getUses().isEmpty()) {
                        Set<IRInst> oUse = new HashSet<>(o.getUses());
                        for (IRInst inst : oUse) {
                            if (visited.contains(inst)) {
                                continue;
                            }
                            visited.add(inst);
                            inst.replaceUse(o, n);
                        }
                    }
                    phi.deleteInst();
                }
            }
            s.getPredecessor().remove(this);
        }
        for (IRInst cur = headInst; cur != null; cur = cur.getNextInst()) {
            cur.destroy();
        }
    }

    public BasicBlock split(IRInst irInst) {
//        System.out.println("Split Bolck: " + currentFunction + " " + this);
        assert irInst.getNextInst() != null;
        BasicBlock newBB = new BasicBlock(name + "_split", currentFunction, depth);
        currentFunction.CheckAndSetName(newBB.name, newBB);
        currentFunction.addBasicBlock(newBB);
        currentFunction.computeDFSListAgain = true;
        currentFunction.computePostDFSListAgain = true;
        currentFunction.computePostReverseDFSListAgain = true;
        newBB.setHeadInst(irInst.getNextInst());
        newBB.setTailInst(tailInst);
        for (IRInst inst = irInst.getNextInst(); inst != null; inst = inst.getNextInst()) {
            inst.setCurrentBB(newBB);
        }
        for (BasicBlock s: successor) {
            s.predecessor.remove(this);
            s.predecessor.add(newBB);
            newBB.successor.add(s);
            for (IRInst phi = s.getHeadInst(); phi instanceof Phi; phi = phi.getNextInst()) {
                Set<Pair<BasicBlock, IROper>> list = new LinkedHashSet<>(((Phi) phi).getPossiblePredecessorSet());
                for (Pair<BasicBlock, IROper> pair: list) {
                    if (pair.getFirst() == this) {
                        ((Phi) phi).getPossiblePredecessorSet().remove(pair);
                        phi.getUses().remove(pair.getSecond());
                        pair.getSecond().getUses().remove(phi);
                        ((Phi) phi).addPair(newBB, pair.getSecond());
                    }
                }
            }
        }
        successor.clear();
        tailInst = irInst;
        assert irInst.getNextInst() != null;
        irInst.getNextInst().setPrevInst(null);
        irInst.setNextInst(null);
//        addInstAtTail(new Br(this, null, newBB, null));
        if (currentFunction.getReturnBB() == this) {
            currentFunction.setReturnBB(newBB);
        }
        return newBB;
    }

    public void mergeWithSuccessor(BasicBlock basicBlock) {
        for (IRInst irInst = basicBlock.headInst; irInst != null; irInst = irInst.getNextInst()) {
            irInst.setCurrentBB(this);
        }
        if (headInst != tailInst) {
            tailInst.deleteInst();
            tailInst.setNextInst(basicBlock.headInst);
            basicBlock.headInst.setPrevInst(tailInst);
            basicBlock.headInst.getPrevInst().setNextInst(basicBlock.headInst);
            tailInst = basicBlock.tailInst;
        } else {
            headInst = basicBlock.headInst;
            tailInst.destroy();
            tailInst = basicBlock.tailInst;
        }

        if (currentFunction.getReturnBB() == basicBlock) {
            assert basicBlock.predecessor.size() == 0;
            currentFunction.setReturnBB(this);
        }
        currentFunction.getBlockSet().remove(basicBlock);
        successor.remove(basicBlock);
        if (basicBlock.successor.isEmpty()) {
            return;
        }
        for (BasicBlock s: basicBlock.successor) {
            s.getPredecessor().remove(basicBlock);
            s.getPredecessor().add(this);
            successor.add(s);
            for (IRInst phi = s.getHeadInst(); phi instanceof Phi; phi = phi.getNextInst()) {
                Set<Pair<BasicBlock, IROper>> list = new LinkedHashSet<>(((Phi) phi).getPossiblePredecessorSet());
                for (Pair<BasicBlock, IROper> pair: list) {
                    if (pair.getFirst() == basicBlock) {
                        ((Phi) phi).getPossiblePredecessorSet().remove(pair);
                        phi.getUses().remove(pair.getSecond());
                        pair.getSecond().getUses().remove(phi);
                        ((Phi) phi).addPair(this, pair.getSecond());
                    }
                }
            }
        }
        basicBlock.successor.clear();
    }
}
