package IR;

import IR.Instruction.*;
import IR.Operand.GlobalVariable;
import IR.Type.StructureType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

public class IRPrinter implements IRVisitor {

    private final OutputStream outputStream = new FileOutputStream("test/test.ll");
    private final PrintWriter printWriter = new PrintWriter(outputStream);
    private final String tab = "    ";

    public IRPrinter() throws FileNotFoundException {
    }

    private void print(String str) {
        printWriter.print(str);
    }

    private void println(String str) {
        printWriter.println(str);
    }

    @Override
    public void visit(Module module) {
        if (module.getStructureTypeMap().size() != 0) {
            for (StructureType structureType: module.getStructureTypeMap().values()) {
                println(structureType.toString());
                println("");
            }
        }

        if (module.getGlobalVariableMap().size() != 0) {
            for (GlobalVariable globalVariable: module.getGlobalVariableMap().values()) {
                println(globalVariable.toString() + " = global " + globalVariable.getValue().getType().toString()
                        + " " + globalVariable.getValue().toString());
            }
        }

        println("");

        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal()) {
                function.accept(this);
            } else {
                println("declare " + function.toString());
            }
            println("");
        }
    }

    @Override
    public void visit(Function function) {
        println("define " + function.toString() + "{");
        int bound = function.getBlockList().size();
        AtomicInteger i = new AtomicInteger(1);
        for (BasicBlock basicBlock: function.getBlockList()) {
            basicBlock.accept(this);
            if (i.get() != bound) {
                println("");
            }
            i.incrementAndGet();
        }
        println("}");
    }

    @Override
    public void visit(BasicBlock basicBlock) {
        print(basicBlock.getName() + ":");
        AtomicInteger counter = new AtomicInteger(basicBlock.getName().length());
        while (counter.get() < 50) {
            print(" ");
            counter.getAndIncrement();
        }
        print("; preds = ");

        AtomicInteger count = new AtomicInteger(1);
        for (BasicBlock pred: basicBlock.getPredecessor()) {
            print(pred.toString());
            if (count.get() < basicBlock.getPredecessor().size()) {
                print(", ");
            }
            count.getAndIncrement();
        }

        println("");

        for (IRInst irInst: basicBlock.getInstList()) {
            println(tab + irInst.toString());
        }
    }

    @Override
    public void visit(Alloca alloca) {
        println(tab + alloca.toString());
    }

    @Override
    public void visit(BinaryOperation binaryOperation) {
        println(tab + binaryOperation.toString());
    }

    @Override
    public void visit(BitCastTo bitCastTo) {
        println(tab + bitCastTo.toString());
    }

    @Override
    public void visit(Br br) {
        println(tab + br.toString());
    }

    @Override
    public void visit(Call call) {
        println(tab + call.toString());
    }

    @Override
    public void visit(GetElementPtr getElementPtr) {
        println(tab + getElementPtr.toString());
    }

    @Override
    public void visit(Icmp icmp) {
        println(tab + icmp.toString());
    }

    @Override
    public void visit(Load load) {
        println(tab + load.toString());
    }

    @Override
    public void visit(Phi phi) {
        println(tab + phi.toString());
    }

    @Override
    public void visit(Store store) {
        println(tab + store.toString());
    }
}
