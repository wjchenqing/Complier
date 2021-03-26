package Codegen;

import Codegen.Operand.GlobalVar;

public interface CodegenVisitor {
    void visit(Module module);
    void visit(Function function);
    void visit(BasicBlock basicBlock);
    void visit(GlobalVar globalVar);
}
