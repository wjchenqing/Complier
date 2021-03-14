package IR.Operand;

import IR.Type.IRType;

public class Register extends IROper {
    private String name;

    public Register(IRType type, String name) {
        super(type);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "%" + name;
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
