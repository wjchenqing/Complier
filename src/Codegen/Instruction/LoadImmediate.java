package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Immediate;
import Codegen.Operand.Register;
import Codegen.Operand.RegisterVirtual;

public class LoadImmediate extends Instruction{
    private final Register rd;
    private final Immediate immediate;

    public LoadImmediate(BasicBlock basicBlock, Register rd, Immediate immediate) {
        super(basicBlock);
        this.rd = rd;
        this.immediate = immediate;
    }

    public Register getRd() {
        return rd;
    }

    public Immediate getImmediate() {
        return immediate;
    }

    @Override
    public void addToUEVarVarKill() {
        basicBlock.addVarKill((RegisterVirtual) rd);
    }
}
