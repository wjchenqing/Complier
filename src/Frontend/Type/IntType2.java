package Frontend.Type;

public class IntType2 extends Type2 {
    public IntType2() {
        typeName = "int";
    }

    @Override
    public boolean equals(Type2 type2) {
        return type2 instanceof IntType2;
    }
}
