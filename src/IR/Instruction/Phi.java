package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
import IR.Operand.IROper;
import IR.Operand.Register;
import Util.Pair;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Phi extends IRInst {
    private Register result;
    private Set<Pair<BasicBlock, IROper>> possiblePredecessorSet;

    public Phi(BasicBlock currentBB, Register result, Set<Pair<BasicBlock, IROper>> possiblePredecessorSet) {
        super(currentBB);
        this.result = result;
        this.possiblePredecessorSet = possiblePredecessorSet;
        defs.add(result);
        result.addDef(this);
        for (Pair<BasicBlock, IROper> pair: possiblePredecessorSet) {
            uses.add(pair.getSecond());
            pair.getSecond().addUse(this);
        }
    }

    public Set<Pair<BasicBlock, IROper>> getPossiblePredecessorSet() {
        return possiblePredecessorSet;
    }

    public void addPair(BasicBlock basicBlock, IROper irOper) {
        possiblePredecessorSet.add(new Pair<>(basicBlock, irOper));
    }

    @Override
    public void replaceUse(IROper o, IROper n) {
        int i = 0;
        Set<Pair<BasicBlock, IROper>> tmp = new HashSet<>(possiblePredecessorSet);
        for (Pair<BasicBlock, IROper> pair: tmp) {
            if (pair.getSecond() == o) {
                uses.remove(o);
                uses.add(n);
                n.addUse(this);
                possiblePredecessorSet.remove(pair);
                possiblePredecessorSet.add(new Pair<>(pair.getFirst(), n));
            }
            ++i;
        }
    }

    @Override
    public Register getResult() {
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(result.toString()).append(" = phi ").append(result.getType().toString()).append(" ");
        int bound = possiblePredecessorSet.size();
        AtomicInteger i = new AtomicInteger(1);
        for (Pair<BasicBlock, IROper> predecessor: possiblePredecessorSet) {
            stringBuilder.append("[ ").append(predecessor.getSecond().toString()).append(", ").append(predecessor.getFirst().toString()).append(" ]");
            if (i.get() != bound) {
                stringBuilder.append(", ");
            }
            i.incrementAndGet();
        }
        return stringBuilder.toString();
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
