package Frontend.Type;

public class VoidType2 extends Type2 {
    public VoidType2() {
        typeName = "void";
    }

    @Override
    public boolean equals(Type2 type2) {
        return type2 instanceof VoidType2;
    }
}
