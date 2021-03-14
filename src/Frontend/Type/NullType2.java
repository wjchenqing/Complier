package Frontend.Type;

public class NullType2 extends WithoutDefaultValue {
    public NullType2() {
        typeName = "null";
    }

    @Override
    public boolean equals(Type2 type2) {
        return type2 instanceof NullType2;
    }
}
