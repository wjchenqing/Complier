package IR.Instruction;

import IR.BasicBlock;
import IR.Operand.IROper;
import IR.Operand.Register;
import IR.Type.IRType;
import javafx.util.Pair;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Phi extends IRInst {
    private Register result;
    private Set<Pair<BasicBlock, IROper>> possibleProcessorSet;

    public Phi(BasicBlock currentBB, Register result, Set<Pair<BasicBlock, IROper>> possibleProcessorSet) {
        super(currentBB);
        this.result = result;
        this.possibleProcessorSet = possibleProcessorSet;
    }

    public Register getResult() {
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(result.toString()).append(" = phi ").append(result.getType().toString()).append(" ");
        int bound = possibleProcessorSet.size();
        AtomicInteger i = new AtomicInteger(1);
        for (Pair<BasicBlock, IROper> processor: possibleProcessorSet) {
            stringBuilder.append("[ ").append(processor.getValue().toString()).append(", ").append(processor.getKey().toString()).append(" ]");
            if (i.get() != bound) {
                stringBuilder.append(", ");
            }
            i.incrementAndGet();
        }
        return stringBuilder.toString();
    }
}
