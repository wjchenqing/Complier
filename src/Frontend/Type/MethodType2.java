package Frontend.Type;

public class MethodType2 extends Type2 {
    private Type2 type2;

    public MethodType2(String name, Type2 type2) {
        typeName = name;
        this.type2 = type2;
    }

    public Type2 getType2() {
        return type2;
    }
}
