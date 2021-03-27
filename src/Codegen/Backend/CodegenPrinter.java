package Codegen.Backend;

import Codegen.BasicBlock;
import Codegen.Function;
import Codegen.Instruction.Instruction;
import Codegen.Module;
import Codegen.Operand.GlobalVar;
import Codegen.CodegenVisitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class CodegenPrinter implements CodegenVisitor {
    private final OutputStream outputStream;
    private final PrintWriter printWriter;
    private final String tab = "    ";

    public CodegenPrinter(String fileName) throws FileNotFoundException {
        outputStream = new FileOutputStream(fileName);
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
        println(tab + ".text");
        println("");

        for (Function function: module.getFunctionMap().values()) {
            function.accept(this);
        }
        println("");

        println(tab + ".section\t.sdata,\"aw\",@progbits");

        for (GlobalVar var: module.getGlobalVarMap().values()) {
            var.accept(this);
        }
    }

    @Override
    public void visit(Function function) {
        if (!function.getIrFunction().isNotExternal()) {
            return;
        }
        println(tab + ".globl" + "  " + function.toString());
        println(tab + ".p2align" + tab + "2");
        println(function.toString() + ":");

        for (BasicBlock basicBlock: function.getBlockList()) {
            basicBlock.accept(this);
        }

        println("");
    }

    @Override
    public void visit(BasicBlock basicBlock) {
        println(basicBlock.printCode() + ":");
        for (Instruction inst: basicBlock.getInstList()) {
            println(inst.printCode());
        }
    }

    @Override
    public void visit(GlobalVar globalVar) {
        switch (globalVar.getVarType()) {
            case String:
                println(globalVar.getIdentifier() + ":");
                println(globalVar.printCode());
                println("");
                break;
            default:
                println(tab + ".globl" + tab + globalVar.getIdentifier());
                println(tab + ".p2align" + tab + "2");
                println(globalVar.getIdentifier() + ":");
                println(globalVar.printCode());
                println("");
                break;
        }
    }
}
