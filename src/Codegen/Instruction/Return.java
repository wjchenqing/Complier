package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.RegisterPhysical;

public class Return extends Instruction {
    public Return(BasicBlock basicBlock, boolean use_a0) {
        super(basicBlock);

        if (use_a0) {
            use.add(RegisterPhysical.getVR(10));
        }
    }

    @Override
    public void addToUEVarVarKill() {

    }

    @Override
    public String toString() {
        return "ret";
    }

    @Override
    public String printCode() {
        return "\tret";
    }
}
