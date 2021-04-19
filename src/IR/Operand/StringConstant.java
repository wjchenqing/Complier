package IR.Operand;

import IR.Type.ArrayType;
import IR.Type.IRType;
import IR.Type.IntegerType;

public class StringConstant extends IROper {
    private final String value;

    public StringConstant(String value) {
        super(null);
        String tmp = value;
        tmp = tmp.replace("\\\\", "\\");
        tmp = tmp.replace("\\n", "\n");
        tmp = tmp.replace("\\\"", "\"");
        tmp = tmp + "\0";
        this.value = tmp;
        type = new ArrayType(tmp.length(), new IntegerType(8));
    }

    @Override
    public String getName() {
        return null;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        String tmp = value;
        tmp = tmp.replace("\\", "\\5C");
        tmp = tmp.replace("\n", "\\0A");
        tmp = tmp.replace("\0", "\\00");
        tmp = tmp.replace("\t", "\\09");
        tmp = tmp.replace("\"", "\\22");
        return "c\"" + tmp + "\"";
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof StringConstant) && (value == ((StringConstant) obj).value);
    }
}
