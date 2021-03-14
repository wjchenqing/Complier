package Frontend.Type;

import IR.Operand.IROper;
import IR.Operand.NullConstant;

abstract public class WithoutDefaultValue extends Type2 {
    @Override
    public IROper defaultOperand() {
        return new NullConstant();
    }
}
