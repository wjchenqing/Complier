package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Function;
import Codegen.Operand.RegisterPhysical;

public class Call extends Instruction {
    static private final int[] callerSavePRNames = {
            1, 5, 6, 7, 10, 11, 12, 13, 14, 15, 16, 17, 28, 29, 30, 31
    };
    private Function callee;

    public Call(BasicBlock basicBlock, Function callee) {
        super(basicBlock);
        this.callee = callee;

        for (int name: callerSavePRNames) {
            def.add(RegisterPhysical.getVR(name));
        }
    }

    @Override
    public void addToUEVarVarKill() {
    }

    @Override
    public String toString() {
        return "call " + callee.getName();
    }

    @Override
    public String printCode() {
        return "\tcall\t" + callee.getName();
    }
}
