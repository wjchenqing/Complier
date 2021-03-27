package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Register;
import Codegen.Operand.RegisterPhysical;
import Codegen.Operand.RegisterVirtual;

public class SetInst extends Instruction {
    static public enum Name {
        seqz, snez, sltz, sgtz
    }
    private final Name name;
    private Register rd;
    private Register rs;

    public SetInst(BasicBlock basicBlock, Name name, Register rd, Register rs) {
        super(basicBlock);
        this.name = name;
        this.rd = rd;
        this.rs = rs;
        def.add((RegisterVirtual) rd);
        use.add((RegisterVirtual) rs);
        use.add(RegisterPhysical.getVR(0));
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
        return name.name() + " " + rd.toString() + ", " + rs.toString();
    }

    @Override
    public String printCode() {
        return "\t" + name.name() + "\t" + rd.printCode() + ", " + rs.printCode();
    }

    public Name getName() {
        return name;
    }

    public Register getRd() {
        return rd;
    }

    public Register getRs() {
        return rs;
    }

    @Override
    public void addToUEVarVarKill() {
        if (!basicBlock.hasVarKill((RegisterVirtual) rs)) {
            basicBlock.addUEVar((RegisterVirtual) rs);
        }
        basicBlock.addVarKill((RegisterVirtual) rd);
    }
}
