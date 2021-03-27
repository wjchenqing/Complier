package Codegen.Operand;

import Codegen.Instruction.Move;

import java.util.HashSet;
import java.util.Set;

public class RegisterVirtual extends Register {
    private Set<RegisterVirtual> adjList = new HashSet<>();
    private int degree;
    private Set<Move> moveList = new HashSet<>();
    private RegisterVirtual alias;
    private RegisterPhysical color;

    public RegisterVirtual(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RegisterVirtual> getAdjList() {
        return adjList;
    }

    public Set<Move> getMoveList() {
        return moveList;
    }

    public void addToAdjList(RegisterVirtual rv) {
        this.adjList.add(rv);
    }

    public void addToMoveList(Move rv) {
        this.moveList.add(rv);
    }

    public int getDegree() {
        return degree;
    }

    public RegisterVirtual getAlias() {
        return alias;
    }

    public RegisterPhysical getColor() {
        return color;
    }

    public void setAlias(RegisterVirtual alias) {
        this.alias = alias;
    }

    public void setColor(RegisterPhysical color) {
        this.color = color;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String printCode() {
        if (color == null) {
            return name;
        } else {
            return color.name;
        }
    }
}
