package IR.Type;

import IR.Operand.BoolConstant;
import IR.Operand.IROper;
import IR.Operand.IntegerConstant;
import IR.Operand.StringConstant;

public class IntegerType extends IRType {
    private int numberOfBits = 0;

    public IntegerType(int numberOfBits) {
        if ((numberOfBits == 1)         // for bool
                || (numberOfBits == 8)  // for String
                || (numberOfBits == 32))// for int
        {
            this.numberOfBits = numberOfBits;
        } else {
            assert false;
        }
    }

    public int getNumberOfBits() {
        return numberOfBits;
    }

    @Override
    public String toString() {
        return "i" + numberOfBits;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof IntegerType) && (numberOfBits == ((IntegerType) obj).getNumberOfBits());
    }

    @Override
    public IROper defaultOperand() {
        switch (numberOfBits) {
            case 1: return new BoolConstant(false);
            case 32: return new IntegerConstant(0);
            default: return super.defaultOperand();
        }
    }

    @Override
    public int getByte() {
        if (numberOfBits == 1) {
            return 1;
        } else if (numberOfBits == 8) {
            return 1;
        } else if (numberOfBits == 32) {
            return 4;
        } else {
            assert false;
            return 0;
        }
    }
}
