package Codegen.Operand;

import Codegen.CodegenVisitor;

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
        stringAsciz = stringAsciz.replace("\\", "\\\\");
        stringAsciz = stringAsciz.replace("\n", "\\n");
        stringAsciz = stringAsciz.replace("\"", "\\\"");

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

    @Override
    public String toString() {
        return identifier;
    }

    @Override
    public String printCode() {
        switch (varType) {
            case Bool:
                return "\t.byte\t" + BoolByte;
            case Int:
                return "\t.word\t" + Integer.toUnsignedLong(IntWord);
            case String:
                return "\t.asciz\t\"" + stringAsciz + "\"";
        }
        return null;
    }

    public void accept(CodegenVisitor visitor) {
        visitor.visit(this);
    }
}
