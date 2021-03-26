package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Immediate;
import Codegen.Operand.Register;
import Codegen.Operand.RegisterVirtual;

public class LoadImmediate extends Instruction{
    private Register rd;
    private final Immediate immediate;

    public LoadImmediate(BasicBlock basicBlock, Register rd, Immediate immediate) {
        super(basicBlock);
        this.rd = rd;
        this.immediate = immediate;
        def.add((RegisterVirtual) rd);
    }

    @Override
    public void replaceDef(RegisterVirtual old, RegisterVirtual n) {
        assert rd == old;
        rd = n;
        super.replaceDef(old, n);
    }

    @Override
    public String toString() {
        return "li " + rd.toString() + ", " + immediate.toString();
    }

    @Override
    public String printCode() {
        return "\tli\t" + rd.printCode() + ", " + immediate.printCode();
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
