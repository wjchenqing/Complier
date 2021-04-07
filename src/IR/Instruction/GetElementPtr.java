package IR.Instruction;

import IR.BasicBlock;
import IR.IRVisitor;
import IR.Operand.GlobalVariable;
import IR.Operand.IROper;
import IR.Operand.Register;
import IR.Type.ArrayType;
import IR.Type.IRType;
import IR.Type.PointerType;

import java.util.ArrayList;

public class GetElementPtr extends IRInst {
    private Register result;
    private IROper pointer;
    private ArrayList<IROper> idxes;

    public GetElementPtr(BasicBlock currentBB, Register result, IROper pointer, ArrayList<IROper> idxes) {
        super(currentBB);

        if (!(pointer.getType() instanceof PointerType)) {
            if (!((pointer instanceof GlobalVariable) && (pointer.getType() instanceof ArrayType))) {
                assert false;
            }
        } else if (!(result.getType() instanceof PointerType)) {
            assert false;
        }

        this.result = result;
        this.pointer = pointer;
        this.idxes = idxes;
        defs.add(result);
        result.addDef(this);
        uses.add(pointer);
        pointer.addUse(this);
        for (IROper irOper: idxes) {
            uses.add(irOper);
            irOper.addUse(this);
        }
    }

    @Override
    public void replaceUse(IROper o, IROper n) {
        if (pointer == o) {
            pointer = n;
            uses.remove(o);
            uses.add(n);
            n.addUse(this);
        }
        int i = 0;
        for (IROper irOper: idxes) {
            if (irOper == o) {
                idxes.set(i, n);
                uses.remove(o);
                uses.add(n);
                n.addUse(this);
            }
            ++i;
        }
    }

    @Override
    public Register getResult() {
        return result;
    }

    public IROper getPointer() {
        return pointer;
    }

    public ArrayList<IROper> getIdxes() {
        return idxes;
    }

    @Override
    public String toString() {
        IRType pointerType;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(result.toString()).append(" = getelementptr ");
        if ((pointer.getType() instanceof PointerType)) {
            stringBuilder.append(((PointerType) pointer.getType()).getType().toString()).append(", ");
            pointerType = pointer.getType();
        } else {
            stringBuilder.append(pointer.getType().toString()).append(", ");
            pointerType = new PointerType(pointer.getType());
        }

        stringBuilder.append(pointerType).append(" ").append(pointer);
        for (IROper idx : idxes) {
            stringBuilder.append(", ").append(idx.getType().toString()).append(" ").append(idx.toString());
        }
        return stringBuilder.toString();
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
