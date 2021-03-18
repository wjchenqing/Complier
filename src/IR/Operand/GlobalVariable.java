package IR.Operand;

import IR.Type.IRType;
import IR.Type.PointerType;

public class GlobalVariable extends IROper {
    private String name;
    private IROper value;

    public GlobalVariable(IRType type, String name, IROper value) {
        super(type);
        this.name = name;
        this.value = value;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public IROper getValue() {
        return value;
    }

    public void setValue(IROper value) {
//        if (value != null) {
//            if (type == null) {
//                type = value.getType();
//            } else {
//                assert type == value.getType();
//            }
//        }
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
