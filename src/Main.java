import AST.Program;
import Codegen.Backend.CodegenPrinter;
import Codegen.Backend.InstructionSelector;
import Codegen.Backend.RegisterAllocator;
import Frontend.ASTBuilder;
import Frontend.SemanticChecker;
import IR.IRBuilder;
import IR.IRPrinter;
import Parser.ErrorListener;
import Parser.MxLexer;
import Parser.MxParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        InputStream inputStream;
        CharStream input = null;
        MxLexer lexer;
        CommonTokenStream tokens;
        MxParser parser;
        ParseTree parseRoot;
        try {
//            inputStream = new FileInputStream("code.txt");
            inputStream = System.in;
            input = CharStreams.fromStream(inputStream);
        } catch (Exception e) {
            assert false;
        }

        lexer = new MxLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ErrorListener());
        tokens = new CommonTokenStream(lexer);
        parser = new MxParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ErrorListener());
        parseRoot = parser.program();

        ASTBuilder astBuilder = new ASTBuilder();
        Program programRoot = (Program) astBuilder.visit(parseRoot);

        SemanticChecker semanticChecker = new SemanticChecker();
        programRoot.accept(semanticChecker);

        if (args.length > 0) {
            return;
        }

        IRBuilder irBuilder = new IRBuilder(semanticChecker.getProgramScope(), semanticChecker.getTypeTable());
        programRoot.accept(irBuilder);

//        IRPrinter irPrinter = new IRPrinter();
//        irBuilder.getModule().accept(irPrinter);
//        irPrinter.getPrintWriter().close();
//        irPrinter.getOutputStream().close();

        InstructionSelector instructionSelector = new InstructionSelector();
        irBuilder.getModule().accept(instructionSelector);

//        CodegenPrinter codegenPrinter_before = new CodegenPrinter("judger/before.s");
//        instructionSelector.getModule().accept(codegenPrinter_before);
//        codegenPrinter_before.getPrintWriter().close();
//        codegenPrinter_before.getOutputStream().close();

        RegisterAllocator registerAllocator = new RegisterAllocator(instructionSelector.getModule());
        registerAllocator.runAll();

        CodegenPrinter codegenPrinter = new CodegenPrinter("output.s");
        instructionSelector.getModule().accept(codegenPrinter);
        codegenPrinter.getPrintWriter().close();
        codegenPrinter.getOutputStream().close();
    }
}
