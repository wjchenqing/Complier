package IR.Type;

public class IntegerType extends IRType {
    private int numberOfBits = 0;

    public IntegerType(int numberOfBits) {
        if ((numberOfBits == 1)         // for bool
                || (numberOfBits == 8)  // for String
                || (numberOfBits == 32))// for int
        {
            this.numberOfBits = numberOfBits;
        } else {
            System.exit(-1);
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
        return (obj instanceof IntegerType) && numberOfBits == ((IntegerType) obj).numberOfBits;
    }
}
