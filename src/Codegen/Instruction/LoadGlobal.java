package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Addr;
import Codegen.Operand.Register;
import Codegen.Operand.RegisterVirtual;

public class LoadGlobal extends Instruction {
    static public enum Name {
        lb, lw
    }
    private final Name name;
    private Register rd;
    private final Addr addr;

    public LoadGlobal(BasicBlock basicBlock, Name name, Register rd, Addr addr) {
        super(basicBlock);
        this.name = name;
        this.rd = rd;
        this.addr = addr;
        def.add((RegisterVirtual) rd);
    }

    @Override
    public String toString() {
        return name.name() + " " + rd.toString() + ", " + addr.toString();
    }

    @Override
    public String printCode() {
        return "\t" + name.name() + "\t" + rd.printCode() + ", " + addr.printCode();
    }

    @Override
    public void replaceDef(RegisterVirtual old, RegisterVirtual n) {
        assert rd == old;
        rd = n;
        super.replaceDef(old, n);
    }

    public Name getName() {
        return name;
    }

    public Register getRd() {
        return rd;
    }

    public Addr getAddr() {
        return addr;
    }

    @Override
    public void addToUEVarVarKill() {
        basicBlock.addVarKill((RegisterVirtual) rd);
    }
}
