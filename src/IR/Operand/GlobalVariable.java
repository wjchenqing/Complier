package IR.Operand;

import IR.Type.PointerType;

public class GlobalVariable extends IROper {
    private final String name;
    private IROper value;

    public GlobalVariable(String name, IROper value) {
        super(value != null ? new PointerType(value.getType()) : null);
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public IROper getValue() {
        return value;
    }

    public void setValue(IROper value) {
        if (value != null) {
            if (type == null) {
                type = value.getType();
            } else {
                assert type == value.getType();
            }
        }
        this.value = value;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
