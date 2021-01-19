package Frontend;

import AST.*;
import Frontend.Entity.VariableEntity;
import Frontend.Scope.BlockScope;
import Frontend.Scope.FunctionScope;
import Frontend.Scope.ProgramScope;
import Frontend.Scope.Scope;

import java.util.ArrayList;
import java.util.Stack;

public class SemanticChecker implements ASTVisitor {
    private ProgramScope programScope = new ProgramScope();
    private Stack<Scope> scopeStack = new Stack<>();

    public Scope getProgramScope() {
        return programScope;
    }

    private Scope currentScope() {
        return scopeStack.peek();
    }

    @Override
    public Object visit(Program node) {
        scopeStack.push(programScope);
        node.setScope(programScope);
        programScope.addBuiltInFunction();
        return null;
    }

    @Override
    public Object visit(Variable node) {
        node.setScope(currentScope());
        if (node.getType().getIdentifier().equals("void"))
            System.exit(-1);

        return null;
    }

    @Override
    public Object visit(Function node) {
        FunctionScope functionScope = new FunctionScope(currentScope());
        scopeStack.push(functionScope);
        node.setScope(programScope);

        node.getType().accept(this);

        return null;
    }

    @Override
    public Object visit(ClassDef node) {
        return null;
    }

    @Override
    public Object visit(PrimitiveType node) {
        node.setScope(currentScope());
        return null;
    }

    @Override
    public Object visit(ClassType node) {
        node.setScope(currentScope());
        return null;
    }

    @Override
    public Object visit(ArrayType node) {
        node.setScope(currentScope());
        return null;
    }

    @Override
    public Object visit(BlockStmt node) {
        BlockScope blockScope = new BlockScope(currentScope());
        scopeStack.push(blockScope);
        node.setScope(blockScope);
        ArrayList<StatementNode> statementNodes = node.getStatements();
        for (StatementNode statementNode: statementNodes) {
            statementNode.accept(this);
        }
        scopeStack.pop();
        return null;
    }

    @Override
    public Object visit(VarDefStmt node) {
        node.setScope(currentScope());
        ArrayList<Variable> variables = node.getVariables();
        for (Variable variable: variables) {
            variable.accept(this);
            currentScope().declareEntity(new VariableEntity(variable.getIdentifier(), variable.getType(), variable.getExpr()));
        }
        return null;
    }

    @Override
    public Object visit(IfStmt node) {
        node.setScope(currentScope());


        return null;
    }

    @Override
    public Object visit(WhileStmt node) {
        return null;
    }

    @Override
    public Object visit(BreakStmt node) {
        return null;
    }

    @Override
    public Object visit(ContinueStmt node) {
        return null;
    }

    @Override
    public Object visit(ReturnStmt node) {
        return null;
    }

    @Override
    public Object visit(ExprStmt node) {
        return null;
    }

    @Override
    public Object visit(ForStmt node) {
        return null;
    }

    @Override
    public Object visit(PostFixExpr node) {
        return null;
    }

    @Override
    public Object visit(NewExpr node) {
        return null;
    }

    @Override
    public Object visit(MemberExpr node) {
        return null;
    }

    @Override
    public Object visit(FuncCallExpr node) {
        return null;
    }

    @Override
    public Object visit(SubScriptExpr node) {
        return null;
    }

    @Override
    public Object visit(PreFixExpr node) {
        return null;
    }

    @Override
    public Object visit(BinaryExpr node) {
        return null;
    }

    @Override
    public Object visit(ThisExpr node) {
        return null;
    }

    @Override
    public Object visit(BoolLiteral node) {
        return null;
    }

    @Override
    public Object visit(IntLiteral node) {
        return null;
    }

    @Override
    public Object visit(StringLiteral node) {
        return null;
    }

    @Override
    public Object visit(NullExpr node) {
        return null;
    }

    @Override
    public Object visit(IdentifierExpr node) {
        return null;
    }

    @Override
    public Object visit(VariableList node) {
        node.setScope(currentScope());
        return null;
    }
}
