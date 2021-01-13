package AST;

abstract public class TypeNode extends ASTNode {
    private String identifier;

    public TypeNode(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
