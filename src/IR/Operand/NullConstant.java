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

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
