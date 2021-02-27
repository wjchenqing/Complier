package IR.Operand;

import IR.Type.PointerType;

public class GlobalVariable extends IROper {
    private final String name;
    private IROper value;

    public GlobalVariable(String name, IROper value) {
        super(new PointerType(value.getType()));
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
        this.value = value;
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
