package Frontend.Type;

abstract public class Type2 {
    protected String typeName;

    public static boolean canAssign(Type2 lType, Type2 rType) {
        if (lType.equals(rType))
            return true;
        else if (lType instanceof ArrayType2 || lType instanceof ClassType2)
            return rType instanceof NullType2;
        else
            return false;
    }

    public boolean equals (Type2 type2) {
        return typeName.equals(type2.typeName);
    }

    public String getTypeName() {
        return typeName;
    }
}
