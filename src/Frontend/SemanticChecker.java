package Frontend;

import AST.*;
import Frontend.Entity.Entity;
import Frontend.Entity.FunctionEntity;
import Frontend.Entity.VariableEntity;
import Frontend.Scope.*;
import Frontend.Type.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class SemanticChecker implements ASTVisitor {
    private final ProgramScope programScope = new ProgramScope();
    private final Stack<Scope> scopeStack = new Stack<>();
    private final TypeTable typeTable = new TypeTable();

    public ProgramScope getProgramScope() {
        return programScope;
    }

    public TypeTable getTypeTable() {
        return typeTable;
    }

    private Scope currentScope() {
        return scopeStack.peek();
    }

    @Override
    public Object visit(Program node) {
        scopeStack.push(programScope);
        node.setScope(programScope);
        programScope.addBuiltInFunction();

        ArrayList<ProgramNode> programNodes = node.getProgramNodes();

        for (ProgramNode programNode: programNodes) {
            if (programNode instanceof ClassDef) {
                ClassType classType = new ClassType(((ClassDef) programNode).getIdentifier());
                ArrayList<VariableEntity> members = new ArrayList<>();
                FunctionEntity cons = null;
                ArrayList<FunctionEntity> methods = new ArrayList<>();
                ArrayList<Variable> variables = ((ClassDef) programNode).getVariables();
                Function constructor = ((ClassDef) programNode).getConstructor();
                ArrayList<Function> functions = ((ClassDef) programNode).getFunctions();
                for (Variable variable : variables)
                    members.add(new VariableEntity(variable.getIdentifier(), variable.getType(), variable.getExpr()));
                if (constructor != null) {
                    ArrayList<VariableEntity> params = new ArrayList<>();
                    for (Variable variable : constructor.getParams()) {
                        params.add(new VariableEntity(variable.getIdentifier(), variable.getType(), variable.getExpr()));
                    }
                    cons = new FunctionEntity(constructor.getIdentifier(), constructor.getType(), params, constructor.getStatement());
                }
                for (Function function : functions) {
                    ArrayList<VariableEntity> params = new ArrayList<>();
                    for (Variable variable : function.getParams()) {
                        params.add(new VariableEntity(variable.getIdentifier(), variable.getType(), variable.getExpr()));
                    }
                    methods.add(new FunctionEntity(function.getIdentifier(), function.getType(), params, function.getStatement()));
                }
                ClassType2 classType2 = new ClassType2(((ClassDef) programNode).getIdentifier(), members, cons, methods);
                typeTable.put(classType, classType2);
            }
        }

        for (ProgramNode programNode: programNodes) {
            if (programNode instanceof Function) {
                ArrayList<VariableEntity> param = new ArrayList<>();
                for (Variable variable: ((Function) programNode).getParams()) {
                    param.add(new VariableEntity(variable.getIdentifier(), variable.getType(), variable.getExpr()));
                }
                programScope.declareEntity(new FunctionEntity(((Function) programNode).getIdentifier(),
                        ((Function) programNode).getType(), param, ((Function) programNode).getStatement()));
            }
        }

        for (ProgramNode programNode: programNodes) {
            if (programNode instanceof Variable) {
                programNode.accept(this);
                programScope.declareEntity(new VariableEntity(((Variable) programNode).getIdentifier(),
                        ((Variable) programNode).getType(), ((Variable) programNode).getExpr()));
            } else if (programNode instanceof ClassDef || programNode instanceof Function) {
                programNode.accept(this);
            }
        }

        Entity mainFunction = programScope.getEntity("main");
        if (!(mainFunction instanceof FunctionEntity)) {
            assert false;
        }
        if (!(((FunctionEntity) mainFunction).getReturnType() instanceof PrimitiveType
                && ((FunctionEntity) mainFunction).getReturnType().getIdentifier().equals("int"))) {
            assert false;
        }
        if (((FunctionEntity) mainFunction).getParams().size() != 0) {
            assert false;
        }

        scopeStack.pop();
        return null;
    }

    @Override
    public Object visit(Variable node) {
        node.setScope(currentScope());
        if (node.getType().getIdentifier().equals("void"))
            assert false;
        node.getType().accept(this);
        ExprNode exprNode = node.getExpr();
        if (exprNode != null) {
            exprNode.accept(this);
            Type2 lType = typeTable.getType2(node.getType());
            Type2 rType = node.getExpr().getType2();
            if (!Type2.canAssign(lType, rType)) {
                assert false;
            }
        }
        return null;
    }

    @Override
    public Object visit(Function node) {
        Map<String, Entity> entityMap = currentScope().getEntityMap();
        String string = node.getIdentifier();
        FunctionEntity functionEntity = (FunctionEntity) entityMap.get(string);

        FunctionScope functionScope = new FunctionScope(currentScope(), node.getType());
        scopeStack.push(functionScope);
        node.setScope(currentScope());

        if (node.getType() != null) {
            node.getType().accept(this);
        } else if (functionEntity == null) {
            assert false;
        }

        ArrayList<Variable> variables = node.getParams();
        ArrayList<VariableEntity> variableEntities = functionEntity.getParams();
        for (int i = 0; i < variableEntities.size(); i++) {
            variables.get(i).accept(this);
            functionScope.declareEntity(variableEntities.get(i));
        }

        node.getStatement().accept(this);

        scopeStack.pop();
        return null;
    }

    @Override
    public Object visit(ClassDef node) {
        ClassType2 classType2 = (ClassType2) typeTable.getType2(new ClassType(node.getIdentifier()));
        ClassScope classScope = new ClassScope(currentScope(), classType2);
        scopeStack.push(classScope);
        node.setScope(classScope);

        ArrayList<Variable> variables = node.getVariables();
        for (Variable variable: variables) {
            variable.accept(this);
            VariableEntity member = new VariableEntity(variable.getIdentifier(),variable.getType(),variable.getExpr());
            classScope.declareEntity(member);
//            classType2.getMembers().add(member);
        }

        ArrayList<Function> functions = node.getFunctions();
        for (Function function: functions) {
            if (function.getIdentifier().equals(node.getIdentifier())) {
                assert false;
            } else {
                ArrayList<VariableEntity> param = new ArrayList<>();
                for (Variable variable: function.getParams()) {
                    param.add(new VariableEntity(variable.getIdentifier(), variable.getType(), variable.getExpr()));
                }
                FunctionEntity method = new FunctionEntity(function.getIdentifier(), function.getType(), param, function.getStatement());
                classScope.declareEntity(method);
                function.setScope(classScope);
//                classType2.getMethods().add(method);
            }
        }

        Function constructor = node.getConstructor();
        if (constructor != null) {
            if (constructor.getParams().size() != 0) {
                assert false;
            } else {
                classScope.declareEntity(new FunctionEntity(node.getIdentifier(), null, new ArrayList<>(), constructor.getStatement()));
                constructor.accept(this);
            }
        }

        for (Function function: functions) {
            if (!function.getIdentifier().equals(node.getIdentifier())) {
                function.accept(this);
            }
        }

        scopeStack.pop();

        return null;
    }

    @Override
    public Object visit(PrimitiveType node) {
        node.setScope(currentScope());
        return null;
    }

    @Override
    public Object visit(ClassType node) {
        if (!typeTable.contains(node))
            assert false;
        node.setScope(currentScope());
        return null;
    }

    @Override
    public Object visit(ArrayType node) {
        if (!typeTable.contains(node.getBaseType()))
            assert false;
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

        ExprNode condition = node.getCondition();
        condition.accept(this);
        if (!condition.getType2().equals(new BoolType2())){
            assert false;
        }

        if (node.getThenBody() != null) {
            if (node.getThenBody() instanceof BlockStmt) {
                node.getThenBody().accept(this);
            } else {
                BlockScope blockScope = new BlockScope(currentScope());
                scopeStack.push(blockScope);
                node.getThenBody().accept(this);
                scopeStack.pop();
            }
        }

        if (node.getElseBody() != null) {
            if (node.getElseBody() instanceof BlockStmt) {
                node.getElseBody().accept(this);
            } else {
                BlockScope blockScope = new BlockScope(currentScope());
                scopeStack.push(blockScope);
                node.getElseBody().accept(this);
                scopeStack.pop();
            }
        }
        return null;
    }

    @Override
    public Object visit(WhileStmt node) {
        node.setScope(currentScope());

        ExprNode condition = node.getExpr();
        condition.accept(this);
        if (!condition.getType2().equals(new BoolType2())){
            assert false;
        }

        if (node.getBody() != null) {
            LoopScope loopScope = new LoopScope(currentScope());
            scopeStack.push(loopScope);
            node.getBody().accept(this);
            scopeStack.pop();
        }
        return null;
    }

    @Override
    public Object visit(BreakStmt node) {
        node.setScope(currentScope());
        if (!currentScope().inLoopScope()) {
            assert false;
        }
        return null;
    }

    @Override
    public Object visit(ContinueStmt node) {
        node.setScope(currentScope());
        if (!currentScope().inLoopScope()) {
            assert false;
        }
        return null;
    }

    @Override
    public Object visit(ReturnStmt node) {
        node.setScope(currentScope());
        if (!currentScope().inFunctionScope()) {
            assert false;
        }

        TypeNode returnType = currentScope().getReturnType();
        Type2 lType = typeTable.getType2(returnType);

        ExprNode exprNode = node.getExpr();
        if (exprNode != null) {
            exprNode.accept(this);
            if (lType == null || lType.equals(new VoidType2())) {
                assert false;
            }
            Type2 rType = exprNode.getType2();
            if (!Type2.canAssign(lType,rType)) {
                assert false;
            }
        } else {
            if (lType != null && !lType.equals(new VoidType2())) {
                assert false;
            }
        }
        return null;
    }

    @Override
    public Object visit(ExprStmt node) {
        node.setScope(currentScope());
        node.getExpr().accept(this);
        return null;
    }

    @Override
    public Object visit(ForStmt node) {
        node.setScope(currentScope());

        ExprNode init = node.getInit();
        if (init != null) {
            init.accept(this);
        }

        ExprNode cond = node.getCond();
        if (cond != null) {
            cond.accept(this);
            if (!cond.getType2().equals(new BoolType2())) {
                assert false;
            }
        }

        ExprNode step = node.getStep();
        if (step != null) {
            step.accept(this);
        }

        StatementNode body = node.getStatement();
        if (body != null) {
            LoopScope loopScope = new LoopScope(currentScope());
            scopeStack.push(loopScope);
            body.accept(this);
            scopeStack.pop();
        }
        return null;
    }

    @Override
    public Object visit(PostFixExpr node) {
        node.setScope(currentScope());
        node.setType2(new IntType2());
        node.setCanBeLValue(false);

        ExprNode exprNode = node.getExpr();
        exprNode.accept(this);
        if (!(exprNode.getType2() instanceof IntType2)) {
            assert false;
        }
        if (!exprNode.isCanBeLValue()) {
            assert false;
        }
        return null;
    }

    @Override
    public Object visit(NewExpr node) {
        node.setScope(currentScope());

        if (node.getType().equals(new PrimitiveType("void"))) {
            assert false;
        }

        node.getType().accept(this);
        Type2 type2 = typeTable.getType2(node.getType());

        if (node.getDim() == 0) {
            if (!(type2 instanceof ClassType2)) {
                assert false;
            }
            node.setType2(type2);
            node.setCanBeLValue(true);
        } else {
            ArrayList<ExprNode> exprNodesForDims = node.getExprPerDim();
            for (ExprNode expr: exprNodesForDims) {
                if (expr == null) {
                    assert false;
                } else {
                    expr.accept(this);
                }
                if (!(expr.getType2() instanceof IntType2)) {
                    assert false;
                }
                node.setType2(new ArrayType2(type2, node.getDim()));
                node.setCanBeLValue(true);
            }
        }
        return null;
    }

    @Override
    public Object visit(MemberExpr node) {
        node.setScope(currentScope());

        ExprNode exprNode = node.getExpr();
        exprNode.accept(this);
        Type2 type2 = exprNode.getType2();
        String identifier = node.getIdentifier();

        if (type2 instanceof ClassType2) {
            if (((ClassType2) type2).hasMember(identifier)) {
                node.setType2(typeTable.getType2(((ClassType2) type2).getMember(identifier).getType()));
                node.setCanBeLValue(true);
            } else if (((ClassType2) type2).hasMethod(identifier)) {
                node.setCanBeLValue(false);
                node.setType2(new MethodType2(identifier, type2));
            } else {
                assert false;
            }
        } else if (type2 instanceof ArrayType2) {
            if (((ArrayType2) type2).hasMethod(identifier)) {
                node.setCanBeLValue(false);
                node.setType2(new MethodType2(identifier, type2));
            } else {
                assert false;
            }
        } else if (type2 instanceof StringType2) {
            if (((StringType2) type2).hasMethod(identifier)) {
                node.setCanBeLValue(false);
                node.setType2(new MethodType2(identifier, type2));
            } else {
                assert false;
            }
        } else {
            assert false;
        }
        return null;
    }

    @Override
    public Object visit(FuncCallExpr node) {
        node.setScope(currentScope());

        ExprNode nameExpr = node.getFuncExpr();
        FunctionEntity functionEntity = null;
        if (nameExpr instanceof MemberExpr) {
            nameExpr.accept(this);
            if (!(nameExpr.getType2() instanceof MethodType2)) {
                assert false;
            }
            Type2 methodType2 = ((MethodType2) nameExpr.getType2()).getType2();
            if (methodType2 instanceof ClassType2) {
                functionEntity = ((ClassType2) methodType2).getMethod(nameExpr.getType2().getTypeName());
            } else if (methodType2 instanceof ArrayType2) {
                functionEntity = ((ArrayType2) methodType2).getMethod(nameExpr.getType2().getTypeName());
            } else if (methodType2 instanceof StringType2) {
                functionEntity = ((StringType2) methodType2).getMethod(nameExpr.getType2().getTypeName());
            } else {
                assert false;
            }
        } else if (nameExpr instanceof IdentifierExpr) {
            Entity entity = currentScope().getEntity(((IdentifierExpr) nameExpr).getIdentifier());
            if ((entity == null) || (entity instanceof VariableEntity)) {
                assert false;
            }
            functionEntity = (FunctionEntity) entity;
        } else {
            assert false;
            functionEntity = null;
        }

        ArrayList<ExprNode> exprNodes = node.getExprNodes();
        for (ExprNode exprNode: exprNodes) {
            exprNode.accept(this);
        }
        ArrayList<VariableEntity> variableEntities = functionEntity == null? new ArrayList<>() : functionEntity.getParams();
        if (exprNodes.size() != variableEntities.size()) {
            assert false;
        }
        for (int i = 0; i < exprNodes.size(); i++) {
            Type2 lType = typeTable.getType2(variableEntities.get(i).getType());
            Type2 rType = exprNodes.get(i).getType2();
            if (!(Type2.canAssign(lType, rType))) {
                assert false;
            }
        }
        node.setCanBeLValue(false);
        node.setType2(typeTable.getType2(functionEntity.getReturnType()));

        return null;
    }

    @Override
    public Object visit(SubScriptExpr node) {
        node.setScope(currentScope());

        ExprNode nameExpr = node.getNameExpr();
        ExprNode dimExpr = node.getDimExpr();
        nameExpr.accept(this);
        dimExpr.accept(this);

        if (!(nameExpr.getType2() instanceof ArrayType2)) {
            assert false;
        }
        if (!(dimExpr.getType2() instanceof IntType2)) {
            assert false;
        }

        node.setCanBeLValue(true);

        Type2 baseType = ((ArrayType2) nameExpr.getType2()).getBaseType();
        int dim = ((ArrayType2) nameExpr.getType2()).getDim();
        if (dim == 1) {
            node.setType2(baseType);
        } else {
            node.setType2(new ArrayType2(baseType, dim - 1));
        }
        return null;
    }

    @Override
    public Object visit(PreFixExpr node) {
        node.setScope(currentScope());

        ExprNode exprNode = node.getExpr();
        PreFixExpr.Operator op = node.getOp();
        exprNode.accept(this);
        Type2 type2 = exprNode.getType2();

        if (op == PreFixExpr.Operator.preFixIncrease || op == PreFixExpr.Operator.preFixDecrease) {
            if (!exprNode.isCanBeLValue() || !(type2 instanceof IntType2)) {
                assert false;
            }
            node.setCanBeLValue(true);
            node.setType2(new IntType2());
        } else if (op == PreFixExpr.Operator.preFixPlus || op == PreFixExpr.Operator.preFixSub || op == PreFixExpr.Operator.bitwiseComplement) {
            if (!(type2 instanceof IntType2)) {
                assert false;
            }
            node.setCanBeLValue(false);
            node.setType2(new IntType2());
        } else if (op == PreFixExpr.Operator.negation) {
            if (!(type2 instanceof BoolType2)) {
                assert false;
            }
            node.setCanBeLValue(false);
            node.setType2(new BoolType2());
        } else {
            assert false;
        }
        return null;
    }

    @Override
    public Object visit(BinaryExpr node) {
        node.setScope(currentScope());

        BinaryExpr.Operator op = node.getOp();
        ExprNode opd1 = node.getOpd1();
        ExprNode opd2 = node.getOpd2();
        opd1.accept(this);
        opd2.accept(this);
        Type2 lType = opd1.getType2();
        Type2 rType = opd2.getType2();

        if (op == BinaryExpr.Operator.multiply
                || op == BinaryExpr.Operator.division
                || op == BinaryExpr.Operator.mod
                || op == BinaryExpr.Operator.sub
                || op == BinaryExpr.Operator.shiftLeft
                || op == BinaryExpr.Operator.shiftRight
                || op == BinaryExpr.Operator.bitWiseAnd
                || op == BinaryExpr.Operator.bitWiseOr
                || op == BinaryExpr.Operator.bitWiseXor) {
            if (!(lType instanceof IntType2) || !(rType instanceof IntType2)) {
                assert false;
            }
            node.setCanBeLValue(false);
            node.setType2(new IntType2());
        } else if (op == BinaryExpr.Operator.plus) {
            if ((lType instanceof IntType2) && (rType instanceof IntType2)) {
                node.setType2(new IntType2());
                node.setCanBeLValue(false);
            } else if ((lType instanceof StringType2) && (rType instanceof StringType2)){
                node.setCanBeLValue(false);
                node.setType2(new StringType2());
            } else {
                assert false;
            }
        } else if (op == BinaryExpr.Operator.less
                    || op == BinaryExpr.Operator.greater
                    || op == BinaryExpr.Operator.lessEqual
                    || op == BinaryExpr.Operator.greaterEqual) {
            if ((lType instanceof IntType2) && (rType instanceof IntType2)) {
                node.setType2(new BoolType2());
                node.setCanBeLValue(false);
            } else if ((lType instanceof StringType2) && (rType instanceof StringType2)){
                node.setCanBeLValue(false);
                node.setType2(new BoolType2());
            } else {
                assert false;
            }
        } else if (op == BinaryExpr.Operator.equal || op == BinaryExpr.Operator.notEqual) {
            if ((Type2.canAssign(lType,rType) && ((lType instanceof BoolType2) || (lType instanceof IntType2) || (lType instanceof NullType2) || (lType instanceof StringType2)))
                || ((lType instanceof ArrayType2) && (rType instanceof NullType2))
                || ((lType instanceof ClassType2) && (rType instanceof NullType2))
                || ((lType instanceof NullType2)  && (rType instanceof ClassType2)
                || ((lType instanceof NullType2)  && (rType instanceof ArrayType2)))) {
                node.setType2(new BoolType2());
                node.setCanBeLValue(false);
            } else {
                assert false;
            }
        } else if (op == BinaryExpr.Operator.and || op == BinaryExpr.Operator.or) {
            if (!(lType instanceof BoolType2) || !(rType instanceof BoolType2)) {
                assert false;
            }
            node.setCanBeLValue(false);
            node.setType2(new BoolType2());
        } else if (op == BinaryExpr.Operator.assign) {
            if (!opd1.isCanBeLValue() || !Type2.canAssign(lType, rType)) {
                assert false;
            }
            node.setCanBeLValue(false);
            node.setType2(lType);
        } else {
            assert false;
        }
        return null;
    }

    @Override
    public Object visit(ThisExpr node) {
        node.setScope(currentScope());
        if (!currentScope().inFunctionScope() || !currentScope().inClassScope()) {
            assert false;
        }
        node.setCanBeLValue(true);
        node.setType2(currentScope().getClassType());
        return null;
    }

    @Override
    public Object visit(BoolLiteral node) {
        node.setScope(currentScope());
        node.setCanBeLValue(false);
        node.setType2(new BoolType2());
        return null;
    }

    @Override
    public Object visit(IntLiteral node) {
        node.setScope(currentScope());
        node.setCanBeLValue(false);
        node.setType2(new IntType2());
        return null;
    }

    @Override
    public Object visit(StringLiteral node) {
        node.setScope(currentScope());
        node.setCanBeLValue(false);
        node.setType2(new StringType2());
        return null;
    }

    @Override
    public Object visit(NullExpr node) {
        node.setScope(currentScope());
        node.setCanBeLValue(false);
        node.setType2(new NullType2());
        return null;
    }

    @Override
    public Object visit(IdentifierExpr node) {
        node.setScope(currentScope());

        Entity entity = currentScope().getEntity(node.getIdentifier());
        if (entity == null) {
            assert false;
        } else if (entity instanceof FunctionEntity) {
            assert false;
        } else {
            node.setCanBeLValue(true);
            node.setType2(typeTable.getType2(((VariableEntity) entity).getType()));
        }
        return null;
    }

    @Override
    public Object visit(VariableList node) {
        node.setScope(currentScope());
        return null;
    }
}
