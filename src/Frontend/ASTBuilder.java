package Frontend;

import AST.*;
import Parser.MxBaseVisitor;
import Parser.MxParser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {

    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        ArrayList<ProgramNode> programNodes = new ArrayList<>();
        for (MxParser.ProgramUnitContext programNode:  ctx.programUnit()) {
            ASTNode node = visit(programNode);
            if (node instanceof VariableList) {
                programNodes.addAll(((VariableList) node).getVariables());
            } else if (node != null) {
                programNodes.add((ProgramNode) node);
            }
        }
        return new Program(programNodes);
    }

    @Override
    public ASTNode visitProgramUnit(MxParser.ProgramUnitContext ctx) {
        if (ctx.funcDef() != null) {
            return visit(ctx.funcDef());
        } else if (ctx.classDef() != null) {
            return visit(ctx.classDef());
        } else if (ctx.varDef() != null) {
            return visit(ctx.varDef());
        } else {
            return null;
        }
    }

    @Override
    public ASTNode visitFuncDef(MxParser.FuncDefContext ctx) {
        TypeNode type;
        if (ctx.type() != null) {
            type = (TypeNode) visit(ctx.type());
        } else {
            type = new PrimitiveType("void");
        }
        ArrayList<Variable> params;
        if (ctx.paramList() != null) {
            params = ((VariableList) visit(ctx.paramList())).getVariables();
        } else {
            params = new ArrayList<>();
        }
        return new Function(type, ctx.IDENTIFIER().getText(), params, (StatementNode) visit(ctx.block()));
    }

    @Override
    public ASTNode visitClassDef(MxParser.ClassDefContext ctx) {
        ArrayList<Variable> variables = new ArrayList<>();
        ArrayList<Function> functions = new ArrayList<>();
        Function constructor;
        for (MxParser.VarDefContext variable: ctx.varDef()) {
            variables.addAll(((VariableList) visit(variable)).getVariables());
        }
        for (MxParser.FuncDefContext funcDefContext: ctx.funcDef()) {
            functions.add((Function) visit(funcDefContext));
        }
        if (ctx.constructorDef().size() >= 2) {
            constructor = null;
            System.exit(-1);
        } else if (ctx.constructorDef().size() == 1) {
            constructor = (Function) visit(ctx.constructorDef(0));
        } else {
            constructor = null;
        }
        return new ClassDef(ctx.IDENTIFIER().getText(), variables, constructor, functions);
    }

    @Override
    public ASTNode visitVarDef(MxParser.VarDefContext ctx) {
        ArrayList<Variable> variables = ((VariableList) visit(ctx.varDefList())).getVariables();
        TypeNode type = (TypeNode) visit(ctx.type());
        for (Variable variable: variables) {
            variable.setType(type);
        }
        return new VariableList(variables);
    }

    @Override
    public ASTNode visitVarDefList(MxParser.VarDefListContext ctx) {
        ArrayList<Variable> variables = new ArrayList<>();
        for (MxParser.VarDefUnitContext varDefUnitContext : ctx.varDefUnit()) {
            variables.add((Variable) visit(varDefUnitContext));
        }
        return new VariableList(variables);
    }

    @Override
    public ASTNode visitVarDefUnit(MxParser.VarDefUnitContext ctx) {
        ExprNode exprNode;
        if (ctx.expr() != null) {
            exprNode = (ExprNode) visit(ctx.expr());
        } else {
            exprNode = null;
        }
        return new Variable(null, ctx.IDENTIFIER().getText(), exprNode);
    }

    @Override
    public ASTNode visitConstructorDef(MxParser.ConstructorDefContext ctx) {
        ArrayList<Variable> params;
        if (ctx.paramList() != null) {
            params = ((VariableList) visit(ctx.paramList())).getVariables();
        } else {
            params = new ArrayList<>();
        }
        return new Function(null, ctx.IDENTIFIER().getText(), params, (StatementNode) visit(ctx.block()));
    }

    @Override
    public ASTNode visitType(MxParser.TypeContext ctx) {
        if (ctx.type() != null) {
            return new ArrayType((TypeNode) visit(ctx.type()));
        } else {
            return visit(ctx.nonArrayType());
        }
    }

    @Override
    public ASTNode visitPrimitiveType(MxParser.PrimitiveTypeContext ctx) {
        if (ctx.BOOL() != null) {
            return new PrimitiveType("bool");
        } else if (ctx.INT() != null) {
            return new PrimitiveType("int");
        } else if (ctx.STRING() != null) {
            return new PrimitiveType("string");
        } else {
            return null;
        }
    }

    @Override
    public ASTNode visitNonArrayType(MxParser.NonArrayTypeContext ctx) {
        if (ctx.primitiveType() != null) {
            return visit(ctx.primitiveType());
        } else {
            return new ClassType(ctx.IDENTIFIER().getText());
        }
    }

    @Override
    public ASTNode visitParamList(MxParser.ParamListContext ctx) {
        ArrayList<Variable> variables = new ArrayList<>();
        for (MxParser.ParamContext paramContext: ctx.param()) {
            variables.add((Variable) visit(paramContext));
        }
        return new VariableList(variables);
    }

    @Override
    public ASTNode visitParam(MxParser.ParamContext ctx) {
        return new Variable((TypeNode) visit(ctx.type()), ctx.IDENTIFIER().getText(), null);
    }

    @Override
    public ASTNode visitBlock(MxParser.BlockContext ctx) {
        ArrayList<StatementNode> statementNodes = new ArrayList<>();
        for (MxParser.StatementContext statementContext: ctx.statement()) {
            ASTNode statement = visit(statementContext);
            if (statement != null) {
                statementNodes.add((StatementNode) statement);
            }
        }
        return new BlockStmt(statementNodes);
    }

    @Override
    public ASTNode visitBlockStmt(MxParser.BlockStmtContext ctx) {
        return visit(ctx.block());
    }

    @Override
    public ASTNode visitVarDefStmt(MxParser.VarDefStmtContext ctx) {
        return new VarDefStmt(((VariableList) visit(ctx.varDef())).getVariables());
    }

    @Override
    public ASTNode visitIfStmt(MxParser.IfStmtContext ctx) {
        StatementNode thenStatement;
        StatementNode elseStatement;
        if (ctx.statement().size() == 1) {
            thenStatement = (StatementNode) visit(ctx.statement(0));
            elseStatement = null;
        } else if (ctx.statement().size() == 2) {
            thenStatement = (StatementNode) visit(ctx.statement(0));
            elseStatement = (StatementNode) visit(ctx.statement(1));
        } else {
            System.exit(-1);
            thenStatement = null;
            elseStatement = null;
        }
        return new IfStmt((ExprNode) visit(ctx.expr()), thenStatement, elseStatement);
    }

    @Override
    public ASTNode visitForStmt(MxParser.ForStmtContext ctx) {
        ExprNode initExpr;
        ExprNode condExpr;
        ExprNode stepExpr;
        if (ctx.init != null) {
            initExpr = (ExprNode) visit(ctx.init);
        } else {
            initExpr = null;
        }
        if (ctx.cond != null) {
            condExpr = (ExprNode) visit(ctx.cond);
        } else {
            condExpr = null;
        }
        if (ctx.step != null) {
            stepExpr = (ExprNode) visit(ctx.step);
        } else {
            stepExpr = null;
        }
        return new ForStmt(initExpr, condExpr, stepExpr, (StatementNode) visit(ctx.statement()));
    }

    @Override
    public ASTNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
        return new WhileStmt((ExprNode) visit(ctx.expr()), (StatementNode) visit(ctx.statement()));
    }

    @Override
    public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return new BreakStmt();
    }

    @Override
    public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return new ContinueStmt();
    }

    @Override
    public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        if (ctx.expr() != null) {
            return new ReturnStmt((ExprNode) visit(ctx.expr()));
        } else {
            return new ReturnStmt(null);
        }
    }

    @Override
    public ASTNode visitExprStmt(MxParser.ExprStmtContext ctx) {
        return new ExprStmt((ExprNode) visit(ctx.expr()));
    }

    @Override
    public ASTNode visitEmptyStmt(MxParser.EmptyStmtContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitNewExpr(MxParser.NewExprContext ctx) {
        return visit(ctx.newInstance());
    }

    @Override
    public ASTNode visitPostfixExpr(MxParser.PostfixExprContext ctx) {
        PostFixExpr.Operator op;
        if (ctx.op.getText().equals("++")) {
            op = PostFixExpr.Operator.postFixIncrease;
        } else {
            op = PostFixExpr.Operator.postFixDecrease;
        }
        return new PostFixExpr(op, (ExprNode) visit(ctx.expr()));
    }

    @Override
    public ASTNode visitThisExpr(MxParser.ThisExprContext ctx) {
        return new ThisExpr();
    }

    @Override
    public ASTNode visitSubscriptExpr(MxParser.SubscriptExprContext ctx) {
        return new SubScriptExpr((ExprNode) visit(ctx.expr(0)), (ExprNode) visit(ctx.expr(1)));
    }

    @Override
    public ASTNode visitMemberExpr(MxParser.MemberExprContext ctx) {
        return new MemberExpr((ExprNode) visit(ctx.expr()), ctx.IDENTIFIER().getText());
    }

    @Override
    public ASTNode visitBinaryExpr(MxParser.BinaryExprContext ctx) {
        ExprNode opd1 = (ExprNode) visit(ctx.opd1);
        ExprNode opd2 = (ExprNode) visit(ctx.opd2);
        switch (ctx.op.getText()) {
            case  "*": return new BinaryExpr(BinaryExpr.Operator.multiply, opd1, opd2);
            case  "/": return new BinaryExpr(BinaryExpr.Operator.division, opd1, opd2);
            case  "%": return new BinaryExpr(BinaryExpr.Operator.mod, opd1, opd2);
            case  "+": return new BinaryExpr(BinaryExpr.Operator.plus, opd1, opd2);
            case  "-": return new BinaryExpr(BinaryExpr.Operator.sub, opd1, opd2);
            case "<<": return new BinaryExpr(BinaryExpr.Operator.shiftLeft, opd1, opd2);
            case ">>": return new BinaryExpr(BinaryExpr.Operator.shiftRight, opd1, opd2);
            case  "<": return new BinaryExpr(BinaryExpr.Operator.less, opd1, opd2);
            case  ">": return new BinaryExpr(BinaryExpr.Operator.greater, opd1, opd2);
            case "<=": return new BinaryExpr(BinaryExpr.Operator.lessEqual, opd1, opd2);
            case ">=": return new BinaryExpr(BinaryExpr.Operator.greaterEqual, opd1, opd2);
            case "==": return new BinaryExpr(BinaryExpr.Operator.equal, opd1, opd2);
            case "!=": return new BinaryExpr(BinaryExpr.Operator.notEqual, opd1, opd2);
            case  "&": return new BinaryExpr(BinaryExpr.Operator.bitWiseAnd, opd1, opd2);
            case  "|": return new BinaryExpr(BinaryExpr.Operator.bitWiseOr, opd1, opd2);
            case  "^": return new BinaryExpr(BinaryExpr.Operator.bitWiseXor, opd1, opd2);
            case "&&": return new BinaryExpr(BinaryExpr.Operator.and, opd1, opd2);
            case "||": return new BinaryExpr(BinaryExpr.Operator.or, opd1, opd2);
            case  "=": return new BinaryExpr(BinaryExpr.Operator.assign, opd1, opd2);
            default  : return null;
        }
    }

    @Override
    public ASTNode visitPrefixExpr(MxParser.PrefixExprContext ctx) {
        ExprNode expr = (ExprNode) visit(ctx.expr());
        switch (ctx.op.getText()) {
            case "++": return new PreFixExpr(PreFixExpr.Operator.preFixIncrease, expr);
            case "--": return new PreFixExpr(PreFixExpr.Operator.preFixDecrease, expr);
            case  "+": return new PreFixExpr(PreFixExpr.Operator.preFixPlus, expr);
            case  "-": return new PreFixExpr(PreFixExpr.Operator.preFixSub, expr);
            case  "!": return new PreFixExpr(PreFixExpr.Operator.negation, expr);
            case  "~": return new PreFixExpr(PreFixExpr.Operator.bitwiseComplement, expr);
            default  : return null;
        }
    }

    @Override
    public ASTNode visitFuncCallExpr(MxParser.FuncCallExprContext ctx) {
        ArrayList<ExprNode> exprNodes;
        if (ctx.exprList() != null){
            exprNodes = new ArrayList<>();
            for (MxParser.ExprContext exprContext: ctx.exprList().expr()) {
                exprNodes.add((ExprNode) visit(exprContext));
            }
        } else {
            exprNodes = new ArrayList<>();
        }
        return new FuncCallExpr((ExprNode) visit(ctx.expr()), exprNodes);
    }

    @Override
    public ASTNode visitSubExpr(MxParser.SubExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public ASTNode visitConstExpr(MxParser.ConstExprContext ctx) {
        return visit(ctx.constant());
    }

    @Override
    public ASTNode visitIdExpr(MxParser.IdExprContext ctx) {
        return new IdentifierExpr(ctx.IDENTIFIER().getText());
    }

    @Override
    public ASTNode visitExprList(MxParser.ExprListContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitWrongNew(MxParser.WrongNewContext ctx) {
        System.exit(-1);
        return null;
    }

    @Override
    public ASTNode visitArrayNew(MxParser.ArrayNewContext ctx) {
        ArrayList<ExprNode> exprNodes = new ArrayList<>();
        int dim = 0;
        for (ParseTree child : ctx.children)
            if (child.getText().equals("["))
                dim++;
        for (MxParser.ExprContext exprContext: ctx.expr()) {
            exprNodes.add((ExprNode) visit(exprContext));
        }
        return new NewExpr((TypeNode) visit(ctx.nonArrayType()), exprNodes, dim);
    }

    @Override
    public ASTNode visitClassNew(MxParser.ClassNewContext ctx) {
        return new NewExpr((TypeNode) visit(ctx.nonArrayType()), null, 0);
    }

    @Override
    public ASTNode visitConstant(MxParser.ConstantContext ctx) {
        if (ctx.BoolLiteral() != null) {
            return new BoolLiteral(ctx.getText().equals("true"));
        } else if (ctx.IntLiteral() != null) {
            return new IntLiteral(Long.parseLong(ctx.getText()));
        } else if (ctx.StringLiteral() != null) {
            return new StringLiteral(ctx.getText());
        } else {
            return new NullExpr();
        }
    }
}
