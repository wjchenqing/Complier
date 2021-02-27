package IR.Instruction;

import IR.BasicBlock;
import IR.Operand.IROper;
import IR.Type.IRType;
import IR.Type.VoidType;

public class Ret extends IRInst {
    private final IRType returnType;
    private final IROper returnVal;

    public Ret(BasicBlock currentBB, IRType returnType, IROper returnVal) {
        super(currentBB);
        if (!(returnType instanceof VoidType)) {
            if (!returnVal.getType().equals(returnType)) {
                System.exit(-1);
            }
        } else {
            if (returnVal != null) {
                System.exit(-1);
            }
        }
        this.returnType = returnType;
        this.returnVal = returnVal;
    }

    @Override
    public String toString() {
        if (!(returnType instanceof VoidType)) {
            return "ret void";
        } else {
            return "ret " + returnType.toString() + " " + returnVal.toString();
        }
    }
}
