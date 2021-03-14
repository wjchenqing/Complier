package IR.Operand;

import IR.Type.IRType;

public class Parameter extends IROper {
    private String paramName;

    public Parameter(IRType type, String paramName) {
        super(type);
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public String toString() {
        return "%" + paramName;
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
