import AST.Program;
import Frontend.ASTBuilder;
import Parser.ErrorListener;
import Parser.MxLexer;
import Parser.MxParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        InputStream inputStream;
        CharStream input = null;
        MxLexer lexer;
        CommonTokenStream tokens;
        MxParser parser;
        ParseTree parseRoot;
        try {
            inputStream = new FileInputStream("code.txt");
//            inputStream = System.in;
            input = CharStreams.fromStream(inputStream);
        } catch (Exception e) {
            System.exit(-1);
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
    }
}
