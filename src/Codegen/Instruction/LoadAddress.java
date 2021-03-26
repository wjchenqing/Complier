package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.GlobalVar;
import Codegen.Operand.Register;
import Codegen.Operand.RegisterVirtual;

public class LoadAddress extends Instruction {
    private Register rd;
    private final GlobalVar globalVar;

    public LoadAddress(BasicBlock basicBlock, Register rd, GlobalVar globalVar) {
        super(basicBlock);
        this.rd = rd;
        this.globalVar = globalVar;
        def.add((RegisterVirtual) rd);
    }

    @Override
    public void replaceDef(RegisterVirtual old, RegisterVirtual n) {
        assert rd == old;
        rd = n;
        super.replaceDef(old, n);
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

    @Override
    public String toString() {
        return "la " + rd.toString() + ", " + globalVar.toString();
    }

    @Override
    public String printCode() {
        return "\tla\t" + rd.printCode() + ", " + globalVar.toString();
    }
}
