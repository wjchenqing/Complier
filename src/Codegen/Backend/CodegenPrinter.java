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
    private final OutputStream outputStream = new FileOutputStream("judger/test.s");
    private final PrintWriter printWriter = new PrintWriter(outputStream);
    private final String tab = "        ";

    public CodegenPrinter() throws FileNotFoundException {
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

        println(tab + ".section\t\".note.GNU-stack\",\"\",@progbits");

        for (GlobalVar var: module.getGlobalVarMap().values()) {
            var.accept(this);
        }
    }

    @Override
    public void visit(Function function) {
        println(tab + ".globl" + "  " + function.toString());
        println(tab + "p2align" + tab + "2");
        println(tab + ".type" + function.toString() + ",@function");
        println(function.toString() + ":");
        println(tab + ".cfi_startproc");

        for (BasicBlock basicBlock: function.getBlockList()) {
            basicBlock.accept(this);
        }

        println(tab + ".cfi_endproc");
        println("");
    }

    @Override
    public void visit(BasicBlock basicBlock) {
        println(basicBlock.printCode() + ":");
        for (Instruction inst: basicBlock.getInstList()) {
            inst.printCode();
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
                println(tab + "p2align" + tab + "2");
                println(globalVar.getIdentifier() + ":");
                println(globalVar.printCode());
                println("");
                break;
        }
    }
}
