package AST;

abstract public class TypeNode extends ASTNode {
    private String identifier;

    public TypeNode(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TypeNode) {
            return identifier.equals(((TypeNode) obj).getIdentifier());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
