package AST;

import Frontend.Type.Type2;
import IR.BasicBlock;

abstract public class ExprNode extends ASTNode {
    protected Type2 type2;
    protected boolean canBeLValue;

    public BasicBlock IfThenBlock = null;
    public BasicBlock IfElseBlock = null;

    public Type2 getType2() {
        return type2;
    }

    public void setType2(Type2 type2) {
        this.type2 = type2;
    }

    public boolean isCanBeLValue() {
        return canBeLValue;
    }

    public void setCanBeLValue(boolean canBeLValue) {
        this.canBeLValue = canBeLValue;
    }
}
