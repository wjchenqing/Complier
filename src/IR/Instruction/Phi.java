package IR.Instruction;

import IR.BasicBlock;
import IR.Operand.IROper;
import IR.Operand.Register;
import Util.Pair;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Phi extends IRInst {
    private Register result;
    private Set<Pair<BasicBlock, IROper>> possiblePredecessorSet;

    public Phi(BasicBlock currentBB, Register result, Set<Pair<BasicBlock, IROper>> possiblePredecessorSet) {
        super(currentBB);
        this.result = result;
        this.possiblePredecessorSet = possiblePredecessorSet;
    }

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
}
