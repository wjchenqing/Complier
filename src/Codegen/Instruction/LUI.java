package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.Immediate;
import Codegen.Operand.RegisterVirtual;

public class LUI extends Instruction{
    private RegisterVirtual rd;
    private Immediate immediate;

    public LUI(BasicBlock basicBlock, RegisterVirtual rd, Immediate immediate) {
        super(basicBlock);
        this.rd = rd;
        this.immediate = immediate;
    }
}
