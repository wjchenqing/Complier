package Frontend.Type;

import IR.Operand.IROper;
import IR.Operand.IntegerConstant;

public class IntType2 extends Type2 {
    public IntType2() {
        typeName = "int";
    }

    @Override
    public boolean equals(Type2 type2) {
        return type2 instanceof IntType2;
    }

    @Override
    public IROper defaultOperand() {
        return new IntegerConstant(0);
    }
}
