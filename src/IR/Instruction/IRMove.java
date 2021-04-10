package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
import IR.Operand.IROper;
import IR.Operand.Register;

public class IRMove extends IRInst {
    private Register result;
    private IROper   source;

    public IRMove(BasicBlock currentBB, Register result, IROper source) {
        super(currentBB);
        this.result = result;
        this.source = source;
        result.getDefs().add(this);
        source.addUse(this);
        defs.add(result);
        uses.add(source);
    }

    public IROper getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "Move " + result.toString() + " " + source.toString();
    }

    @Override
    public Register getResult() {
        return result;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void replaceUse(IROper o, IROper n) {
        if (source == o) {
            uses.add(n);
            uses.remove(o);
            o.getUses().remove(this);
            n.getUses().add(this);
        }
    }
}
