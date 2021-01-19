package Frontend.Type;

public class NullType2 extends Type2 {
    public NullType2() {
        typeName = "null";
    }

    @Override
    public boolean equals(Type2 type2) {
        return true;
    }
}
