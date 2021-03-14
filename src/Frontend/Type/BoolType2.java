package Frontend.Type;

import IR.Operand.BoolConstant;
import IR.Operand.IROper;

public class BoolType2 extends Type2 {
    public BoolType2() {
        typeName = "bool";
    }

    @Override
    public boolean equals(Type2 type2) {
        return type2 instanceof BoolType2;
    }

    @Override
    public IROper defaultOperand() {
        return new BoolConstant(false);
    }
}
