package Codegen.Operand;

public class RegisterVirtual extends Register {
    private String name;

    public RegisterVirtual(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
