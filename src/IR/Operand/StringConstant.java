package IR.Operand;

import IR.Type.IRType;

public class StringConstant extends IROper {
    private final String value;

    public StringConstant(IRType type, String value) {
        super(type);
        this.value = value;
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
}
