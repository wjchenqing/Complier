package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Register;
import Codegen.Operand.RegisterVirtual;

public class Move extends Instruction {
    private Register rd;
    private Register rs;

    public Move(BasicBlock basicBlock, Register rd, Register rs) {
        super(basicBlock);
        this.rd = rd;
        this.rs = rs;
        def.add((RegisterVirtual) rd);
        use.add((RegisterVirtual) rs);
    }

    public Register getRd() {
        return rd;
    }

    public Register getRs() {
        return rs;
    }

    @Override
    public void replaceDef(RegisterVirtual old, RegisterVirtual n) {
        assert rd == old;
        rd = n;
        super.replaceDef(old, n);
    }

    @Override
    public void replaceUse(RegisterVirtual old, RegisterVirtual n) {
        assert rs == old;
        rs = n;
        super.replaceUse(old, n);
    }

    @Override
    public String toString() {
        return "mv " + rd.toString() + ", " + rs.toString();
    }

    @Override
    public String printCode() {
        return "\tmv\t" + rd.printCode() + ", " + rs.printCode();
    }

    @Override
    public void addToUEVarVarKill() {
        if (!basicBlock.hasVarKill((RegisterVirtual) rs)) {
            basicBlock.addUEVar((RegisterVirtual) rs);
        }
        basicBlock.addVarKill((RegisterVirtual) rd);
    }
}
