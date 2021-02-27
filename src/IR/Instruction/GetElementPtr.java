package IR.Instruction;

import IR.BasicBlock;
import IR.Operand.IROper;
import IR.Operand.Register;
import IR.Type.PointerType;

import java.util.ArrayList;

public class GetElementPtr extends IRInst {
    private Register result;
    private IROper pointer;
    private ArrayList<IROper> idxes;

    public GetElementPtr(BasicBlock currentBB, Register result, IROper pointer, ArrayList<IROper> idxes) {
        super(currentBB);

        assert pointer.getType() instanceof PointerType;
        assert result.getType() instanceof PointerType;

        this.result = result;
        this.pointer = pointer;
        this.idxes = idxes;
    }

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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(result.toString()).append(" = getelementptr ");
        stringBuilder.append(((PointerType) pointer.getType()).getType().toString()).append(", ");
        stringBuilder.append(pointer.getType()).append(" ").append(pointer);
        for (IROper idx : idxes) {
            stringBuilder.append(", ").append(idx.getType().toString()).append(" ").append(idx.toString());
        }
        return stringBuilder.toString();
    }
}
