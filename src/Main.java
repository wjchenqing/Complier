import AST.Program;
import Codegen.Backend.CodegenPrinter;
import Codegen.Backend.InstructionSelector;
import Codegen.Backend.RegisterAllocator;
import Frontend.ASTBuilder;
import Frontend.SemanticChecker;
import IR.IRBuilder;
import IR.IRPrinter;
import Opt.*;
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

        if (args.length == 0) {
            return;
        }

        IRBuilder irBuilder = new IRBuilder(semanticChecker.getProgramScope(), semanticChecker.getTypeTable());
        programRoot.accept(irBuilder);

//        IRPrinter irPrinter1 = new IRPrinter("judger/test_before.ll");
//        irBuilder.getModule().accept(irPrinter1);
//        irPrinter1.getPrintWriter().close();
//        irPrinter1.getOutputStream().close();

        CFGSimplifier cfgSimplifier = new CFGSimplifier(irBuilder.getModule());
        cfgSimplifier.run();

//        IRPrinter irPrinter2 = new IRPrinter("judger/test_cfg.ll");
//        irBuilder.getModule().accept(irPrinter2);
//        irPrinter2.getPrintWriter().close();
//        irPrinter2.getOutputStream().close();

        DominatorTree dominatorTree = new DominatorTree(irBuilder.getModule());
        dominatorTree.run();

        SSAConstructor ssaConstructor = new SSAConstructor(irBuilder.getModule());
        ssaConstructor.run();

//        IRPrinter irPrinter4 = new IRPrinter("judger/test_ssa.ll");
//        irBuilder.getModule().accept(irPrinter4);
//        irPrinter4.getPrintWriter().close();
//        irPrinter4.getOutputStream().close();

        SideEffectChecker sideEffectChecker = new SideEffectChecker(irBuilder.getModule());
        DeadCodeEliminator deadCodeEliminator = new DeadCodeEliminator(irBuilder.getModule(), sideEffectChecker);
        Inline inline = new Inline(irBuilder.getModule(), cfgSimplifier);

        boolean changed = true;
        while (changed) {
            changed = false;
            changed |= deadCodeEliminator.run();
        }

//        IRPrinter irPrinter4 = new IRPrinter("judger/test_ssa.ll");
//        irBuilder.getModule().accept(irPrinter4);
//        irPrinter4.getPrintWriter().close();
//        irPrinter4.getOutputStream().close();



        changed = true;
//        int cnt = 0;
        while (changed) {
//            if (cnt > 0) {
//                break;
//            }
//            ++cnt;
//            System.out.println(cnt);
            changed = false;
            dominatorTree.run();
            changed |= deadCodeEliminator.run();
            changed |= inline.run();
        }

//        IRPrinter irPrinter4 = new IRPrinter("judger/test_ssa.ll");
//        irBuilder.getModule().accept(irPrinter4);
//        irPrinter4.getPrintWriter().close();
//        irPrinter4.getOutputStream().close();
//
//        changed |= deadCodeEliminator.run();

//        irBuilder.getModule().removeUselessFunc();

//        IRPrinter irPrinter = new IRPrinter("judger/test.ll");
//        irBuilder.getModule().accept(irPrinter);
//        irPrinter.getPrintWriter().close();
//        irPrinter.getOutputStream().close();

        irBuilder.getModule().CheckBranch();

        SSADestructor ssaDestructor = new SSADestructor(irBuilder.getModule());
        ssaDestructor.run();
        cfgSimplifier.run();

//        IRPrinter irPrinter3 = new IRPrinter("judger/test_to_codegen.ll");
//        irBuilder.getModule().accept(irPrinter3);
//        irPrinter3.getPrintWriter().close();
//        irPrinter3.getOutputStream().close();

        InstructionSelector instructionSelector = new InstructionSelector();
        irBuilder.getModule().accept(instructionSelector);

//        CodegenPrinter codegenPrinter_before = new CodegenPrinter("judger/before.s");
//        instructionSelector.getModule().accept(codegenPrinter_before);
//        codegenPrinter_before.getPrintWriter().close();
//        codegenPrinter_before.getOutputStream().close();

        RegisterAllocator registerAllocator = new RegisterAllocator(instructionSelector.getModule());
        registerAllocator.runAll();

        CodegenPrinter codegenPrinter = new CodegenPrinter("output.s");
//        CodegenPrinter codegenPrinter = new CodegenPrinter("judger/test.s");
        instructionSelector.getModule().accept(codegenPrinter);
        codegenPrinter.getPrintWriter().close();
        codegenPrinter.getOutputStream().close();
    }
}
