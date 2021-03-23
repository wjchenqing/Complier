package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.GlobalVar;
import Codegen.Operand.Register;
import Codegen.Operand.RegisterVirtual;

public class LoadAddress extends Instruction {
    private final Register rd;
    private final GlobalVar globalVar;

    public LoadAddress(BasicBlock basicBlock, Register rd, GlobalVar globalVar) {
        super(basicBlock);
        this.rd = rd;
        this.globalVar = globalVar;
    }

    public Register getRd() {
        return rd;
    }

    public GlobalVar getGlobalVar() {
        return globalVar;
    }

    @Override
    public void addToUEVarVarKill() {
        basicBlock.addVarKill((RegisterVirtual) rd);
    }
}
