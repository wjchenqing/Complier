package IR;

import IR.Instruction.*;

public interface IRVisitor {

    void visit(Module module);
    void visit(Function function);
    void visit(BasicBlock basicBlock);

    void visit(Alloca alloca);
    void visit(BinaryOperation binaryOperation);
    void visit(BitCastTo bitCastTo);
    void visit(Br br);
    void visit(Call call);
    void visit(GetElementPtr getElementPtr);
    void visit(Icmp icmp);
    void visit(Load load);
    void visit(Phi phi);
    void visit(Store store);

}
