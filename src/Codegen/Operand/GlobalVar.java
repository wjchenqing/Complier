package Codegen.Operand;

public class GlobalVar extends Operand {
    static public enum VarType {
        String, Int, Bool
    }
    private final String identifier;
    private VarType varType;
    private String stringAsciz;
    private int IntWord;
    private int BoolByte;

    public GlobalVar(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public VarType getVarType() {
        return varType;
    }

    public String getStringAsciz() {
        return stringAsciz;
    }

    public int getIntWord() {
        return IntWord;
    }

    public int getBoolByte() {
        return BoolByte;
    }

    public void setStringAsciz(String stringAsciz) {
        this.stringAsciz = stringAsciz;
        this.varType = VarType.String;
    }

    public void setIntWord(int intWord) {
        IntWord = intWord;
        this.varType = VarType.Int;
    }

    public void setBoolByte(int boolByte) {
        BoolByte = boolByte;
        this.varType = VarType.Bool;
    }
}
