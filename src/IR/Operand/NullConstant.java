package IR.Operand;

import IR.Type.PointerType;
import IR.Type.VoidType;

public class NullConstant extends IROper {
    public NullConstant() {
        super(new PointerType(new VoidType()));
    }

    @Override
    public String toString() {
        return "null";
    }
}
