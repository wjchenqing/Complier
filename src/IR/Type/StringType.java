package IR.Type;

public class StringType extends ArrayType {

    public StringType(int elements) {
        super(elements, new IntegerType(8));
    }

    @Override
    public int getByte() {
        return super.getByte();
    }
}
