package IR.Instruction;

import IR.BasicBlock;
import IR.Operand.IROper;
import IR.Operand.Register;
import javafx.util.Pair;

import java.util.Set;

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
        return null; //todo
    }
}
