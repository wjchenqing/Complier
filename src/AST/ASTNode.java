package AST;

abstract public class ASTNode {

    abstract public Object accept(ASTVisitor visitor);
}
