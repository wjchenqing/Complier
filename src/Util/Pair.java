package Util;

public class Pair<Type1, Type2> {
    private Type1 first;
    private Type2 second;

    public Pair(Type1 first, Type2 second) {
        this.first = first;
        this.second = second;
    }

    public Type1 getFirst() {
        return first;
    }

    public Type2 getSecond() {
        return second;
    }

    public void setFirst(Type1 first) {
        this.first = first;
    }

    public void setSecond(Type2 second) {
        this.second = second;
    }
}