package AST;

import java.util.ArrayList;

public class Program extends ASTNode {
    private ArrayList<ProgramNode> ProgramNodes;

    public Program(ArrayList<ProgramNode> programNodes) {
        ProgramNodes = programNodes;
    }

    public ArrayList<ProgramNode> getProgramNodes() {
        return ProgramNodes;
    }

    public void addProgramNode (ProgramNode programNode) {
        ProgramNodes.add(programNode);
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
