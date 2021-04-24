package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
import IR.Operand.GlobalVariable;
import IR.Operand.IROper;
import IR.Operand.NullConstant;
import IR.Operand.Register;
import IR.Type.IRType;
import IR.Type.PointerType;
import IR.Type.VoidType;

public class Store extends IRInst {
    private IROper value;
    private IROper pointer;

    public Store(BasicBlock currentBB, IROper value, IROper pointer) {
        super(currentBB);

        if (!(pointer instanceof GlobalVariable)) {
            if (!(pointer.getType() instanceof PointerType)) {
                assert false;
            }
            if (!value.getType().equals(new PointerType(new VoidType()))) {
                if (!value.getType().equals(((PointerType) pointer.getType()).getType())) {
                    assert false;
                }
            }
        }
        this.value = value;
        this.pointer = pointer;
        uses.add(value);
        uses.add(pointer);
        value.addUse(this);
        pointer.addUse(this);
    }

    @Override
    public void replaceUse(IROper o, IROper n) {
        if (value == o) {
            value = n;
            uses.remove(o);
            uses.add(n);
            o.getUses().remove(this);
            n.addUse(this);
        }
        if (pointer == o) {
            pointer = n;
            uses.remove(o);
            uses.add(n);
            o.getUses().remove(this);
            n.addUse(this);
        }
    }

    public IROper getValue() {
        return value;
    }

    public IROper getPointer() {
        return pointer;
    }

    @Override
    public String toString() {
        IRType type1 = (pointer instanceof GlobalVariable) ? new PointerType(pointer.getType()) : pointer.getType();
        IRType type = (value instanceof NullConstant) ? (((PointerType) type1)).getType(): value.getType();
        return "store " + type.toString() + " " + value.toString() + ", "
                + type1.toString() + " " + pointer.toString();
    }

    @Override
    public Register getResult() {
        return null;
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
