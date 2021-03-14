package Frontend.Entity;

import IR.Operand.IROper;

abstract public class Entity{
    private final String name;

    private IROper addr;

    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public IROper getAddr() {
        return addr;
    }

    public void setAddr(IROper addr) {
        this.addr = addr;
    }
}
