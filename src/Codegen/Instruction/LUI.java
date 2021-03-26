package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Immediate;
import Codegen.Operand.RegisterVirtual;

public class LUI extends Instruction{
    private RegisterVirtual rd;
    private Immediate immediate;

    public LUI(BasicBlock basicBlock, RegisterVirtual rd, Immediate immediate) {
        super(basicBlock);
        this.rd = rd;
        this.immediate = immediate;
        def.add(rd);
    }

    @Override
    public void replaceDef(RegisterVirtual old, RegisterVirtual n) {
        assert rd == old;
        rd = n;
        super.replaceDef(old, n);
    }

    @Override
    public String toString() {
        return "lui " + rd.toString() + ", " + immediate.toString();
    }

    @Override
    public String printCode() {
        return "\tlui\t" + rd.printCode() + ", " + immediate.printCode();
    }

    @Override
    public void addToUEVarVarKill() {
        basicBlock.addVarKill(rd);
    }
}
