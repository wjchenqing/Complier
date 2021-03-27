package Util;

import Codegen.Operand.RegisterVirtual;

public class Edge extends Pair<RegisterVirtual, RegisterVirtual> {
    public Edge(RegisterVirtual first, RegisterVirtual second) {
        super(first, second);
        if (first.hashCode() < second.hashCode()) {
            setFirst(second);
            setSecond(first);
        }
    }

    @Override
    public String toString() {
        return "(" + getFirst().getName() + ", " + getSecond().getName() + ")";
    }
}
