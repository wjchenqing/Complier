package AST;

import Frontend.Scope.Scope;

abstract public class ASTNode {
    protected Scope scope = null;

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    abstract public Object accept(ASTVisitor visitor);
}
