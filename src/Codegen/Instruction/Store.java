package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Addr;
import Codegen.Operand.Register;
import Codegen.Operand.RegisterVirtual;

public class Store extends Instruction {
    static public enum Name {
        sb, sw
    }
    private final Name name;
    private Register rs;
    private final Addr addr;

    public Store(BasicBlock basicBlock, Name name, Register rs, Addr addr) {
        super(basicBlock);
        this.name = name;
        this.rs = rs;
        this.addr = addr;
        use.add((RegisterVirtual) rs);
        if (!addr.isStackLocation()) {
            use.add((RegisterVirtual) addr.getBase());
        }
    }

    @Override
    public void replaceUse(RegisterVirtual old, RegisterVirtual n) {
        if (rs == old) {
            rs = n;
        } else {
            assert !addr.isStackLocation();
            assert addr.getBase() == old;
            addr.setBase(n);
        }
        super.replaceUse(old, n);
    }

    @Override
    public String toString() {
        return name.name() + " " + rs.toString() + ", " + addr.toString();
    }

    @Override
    public String printCode() {
        return "\t" + name.name() + "\t" + rs.printCode() + ", " + addr.printCode();
    }

    public Name getName() {
        return name;
    }

    public Register getRs() {
        return rs;
    }

    public Addr getAddr() {
        return addr;
    }

    @Override
    public void addToUEVarVarKill() {
        if (!basicBlock.hasVarKill((RegisterVirtual) rs)) {
            basicBlock.addUEVar((RegisterVirtual) rs);
        }
        if (addr.isStackLocation()) {
            return;
        }
        if (!basicBlock.hasVarKill((RegisterVirtual) addr.getBase())) {
            basicBlock.addUEVar((RegisterVirtual) addr.getBase());
        }
    }
}
