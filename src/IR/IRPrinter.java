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

//    private final OutputStream outputStream = new FileOutputStream("cqqqq.ll");
    private final OutputStream outputStream;
    private final PrintWriter printWriter;
    private final String tab = "    ";

    public IRPrinter(String name) throws FileNotFoundException {
        outputStream = new FileOutputStream(name);
        printWriter = new PrintWriter(outputStream);
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    private void print(String str) {
        printWriter.print(str);
    }

    private void println(String str) {
        printWriter.println(str);
    }

    @Override
    public void visit(Module module) {
        println("target triple = \"x86_64-pc-linux-gnu\"");
        println("");
        if (module.getStructureTypeMap().size() != 0) {
            for (StructureType structureType: module.getStructureTypeMap().values()) {
                println(structureType.printString());
                println("");
            }
        }

        if (module.getGlobalVariableMap().size() != 0) {
            for (GlobalVariable globalVariable: module.getGlobalVariableMap().values()) {
                println(globalVariable.toString() + " = global " + globalVariable.getType().toString()
                        + " " + globalVariable.getValue().toString());
            }
            println("");
        }

        if (module.getStringConstMapSize() != 0) {
            for (GlobalVariable stringConst: module.getStringConstMap().values()) {
                println(stringConst.toString() + " = private unnamed_addr constant " + stringConst.getValue().getType().toString()
                        + " " + stringConst.getValue().toString());
            }
            println("");
        }


        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal()) {
                function.accept(this);
            } else {
                println("declare " + function.printer());
            }
            println("");
        }
    }

    @Override
    public void visit(Function function) {
        println("define " + function.printer() + "{");
        for (BasicBlock basicBlock: function.getDfsList()) {
            basicBlock.accept(this);
            println("");
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
        if (basicBlock.getPredecessor().size() != 0) {
            print("; preds = ");
        }

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

    @Override
    public void visit(Ret ret) {

    }

    @Override
    public void visit(IRMove IRMove) {

    }
}
