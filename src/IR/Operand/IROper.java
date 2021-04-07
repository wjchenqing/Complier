package IR.Operand;

import IR.Instruction.IRInst;
import IR.Type.IRType;

import java.util.HashSet;
import java.util.Set;

abstract public class IROper implements Cloneable {
    protected IRType type;

    protected Set<IRInst> uses = new HashSet<>();
    protected Set<IRInst> defs = new HashSet<>();

    public IROper(IRType type) {
        this.type = type;
    }

    public IRType getType() {
        return type;
    }

    public void setName(String name) {}

    abstract public String getName();

    @Override
    abstract public String toString();

    abstract public boolean isConstant();

    public Set<IRInst> getUses() {
        return uses;
    }

    public Set<IRInst> getDefs() {
        return defs;
    }

    public void addUse(IRInst irInst) {
        uses.add(irInst);
    }

    public void addDef(IRInst irInst) {
        defs.add(irInst);
    }

    public void replaceUse(IROper n) {
        for (IRInst irInst: uses) {
            irInst.replaceUse(this, n);
        }
    }
}
