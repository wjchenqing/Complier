package IR;

import AST.*;
import AST.ArrayType;
import AST.Function;
import Frontend.Entity.Entity;
import Frontend.Entity.VariableEntity;
import Frontend.Scope.ProgramScope;
import Frontend.Scope.Scope;
import Frontend.Type.*;
import IR.Instruction.*;
import IR.Operand.*;
import IR.Type.*;
import Util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class IRBuilder implements ASTVisitor {

    private final Module module;
    private final ProgramScope programScope;
    private final TypeTable astTypeTable;

    private IR.Function currentFunction = null;
    private BasicBlock  currentBB = null;

    private final Stack<BasicBlock> loopExitBlocks = new Stack<>();
    private final Stack<BasicBlock> loopContinueBlocks = new Stack<>();

    public IRBuilder(ProgramScope programScope, TypeTable astTypeTable) {
        this.programScope = programScope;
        this.astTypeTable = astTypeTable;
        module = new Module(astTypeTable);
    }

    public Module getModule() {
        return module;
    }

    public ProgramScope getProgramScope() {
        return programScope;
    }

    public TypeTable getAstTypeTable() {
        return astTypeTable;
    }

    public IR.Function getCurrentFunction() {
        return currentFunction;
    }

    public BasicBlock getCurrentBB() {
        return currentBB;
    }

    public void setCurrentFunction(IR.Function currentFunction) {
        this.currentFunction = currentFunction;
    }

    public void setCurrentBB(BasicBlock currentBB) {
        this.currentBB = currentBB;
    }

    @Override
    public Object visit(Program node) {
        for (ProgramNode programNode: node.getProgramNodes()) {
            if (programNode instanceof ClassDef) {
                if (((ClassDef) programNode).getConstructor() != null)
                    module.addFunction(((ClassDef) programNode).getConstructor());
                for (Function function: ((ClassDef) programNode).getFunctions())
                    module.addFunction(function);
            }
        }

        for (ProgramNode programNode: node.getProgramNodes()) {
            if (programNode instanceof Function) {
                module.addFunction((Function) programNode);
            }
        }

        currentFunction = new IR.Function(module,"_MxProgramInitial", new VoidType(),
                new FunctionType(new VoidType(), new ArrayList<>()), new ArrayList<>(), true);
        currentBB = currentFunction.getHeadBB();
        module.addFunction(currentFunction);
        for (ProgramNode programNode: node.getProgramNodes()) {
            if (programNode instanceof Variable)
                programNode.accept(this);
        }
        currentBB.addInstAtTail(new Br(currentBB, null, currentFunction.getReturnBB(), null));
        currentFunction = null;
        currentBB = null;

        for (ProgramNode programNode: node.getProgramNodes()) {
            if (programNode instanceof ClassDef) {
                programNode.accept(this);
            }
        }

        for (ProgramNode programNode: node.getProgramNodes()) {
            if (programNode instanceof Function) {
                programNode.accept(this);
            }
        }

        return null;
    }

    @Override
    public Object visit(Variable node) { //Maybe Done
        String identifier = node.getIdentifier();
        Type2 type2 = astTypeTable.getType2(node.getType());
        IRType type = module.getIrTypeTable().get(type2);
        if (type instanceof StructureType) {
            type = new PointerType(type);
        }
        Scope scope = node.getScope();
        VariableEntity variableEntity = (VariableEntity) scope.getEntity(identifier);
        if (scope instanceof ProgramScope) {
            IROper value = type2.defaultOperand();
            GlobalVariable globalVariable = new GlobalVariable(type, identifier, null);
            if (node.getExpr() != null) {
                Object result = node.getExpr().accept(this);
                assert result instanceof ExprResultPair;
                value = ((ExprResultPair) result).getResult();
                if (!(value.isConstant())) {
                    currentBB.addInstAtTail(new Store(currentBB, value, globalVariable));
                    value = type2.defaultOperand();
                }
            }
            globalVariable.setValue(value);
            module.addGlobalVariable(globalVariable);
            variableEntity.setAddr(globalVariable);
        } else {
            ArrayList<IROper> paramForMalloc = new ArrayList<>();
            paramForMalloc.add(new IntegerConstant(new PointerType(type).getByte()));
            IR.Function mallocFunction = module.getFunction("malloc");
//            Register mallocAddr = new Register(new PointerType(new IntegerType(8)), "mallocAddr");
//            currentFunction.CheckAndSetName(mallocAddr.getName(), mallocAddr);
            Register addr = new Register(new PointerType(type), identifier);
            BasicBlock headBB = currentFunction.getHeadBB();
            headBB.addInstAtHead(new Store(headBB, type2.defaultOperand(), addr));
//            headBB.addInstAtHead(new BitCastTo(headBB, addr, mallocAddr, new PointerType(type)));
            headBB.addInstAtHead(new Call(headBB, addr, mallocFunction, paramForMalloc));
//            ArrayList<IROper> paramForMalloc = new ArrayList<>();
//            paramForMalloc.add(new IntegerConstant(new PointerType(type).getByte()));
//            IR.Function mallocFunction = module.getFunction("malloc");
//            Register mallocAddr = new Register(new PointerType(new IntegerType(8)), "mallocAddr");
//            currentFunction.CheckAndSetName(mallocAddr.getName(), mallocAddr);
//            Register addr = new Register(new PointerType(type), identifier);
//            BasicBlock headBB = currentFunction.getHeadBB();
//            headBB.addInstAtHead(new Store(headBB, type2.defaultOperand(), addr));
//            headBB.addInstAtHead(new BitCastTo(headBB, addr, mallocAddr, new PointerType(type)));
//            headBB.addInstAtHead(new Call(headBB, mallocAddr, mallocFunction, paramForMalloc));
//            headBB.addInstAtHead(new Store(headBB, type2.defaultOperand(), addr));
//            headBB.addInstAtHead(new Alloca(headBB, addr, type));
            currentFunction.CheckAndSetName(identifier, addr);
            variableEntity.setAddr(addr);
            if (node.getExpr() != null) {
                Object result = node.getExpr().accept(this);
                assert result instanceof ExprResultPair;
                currentBB.addInstAtTail(new Store(currentBB, ((ExprResultPair) result).getResult(), addr));
            }
        }
        return null;
    }

    @Override
    public Object visit(Function node) {
        String identifier = node.getIdentifier();
        if (node.getScope().inClassScope())
            identifier = node.getScope().getClassType().getTypeName() + "." + identifier;

        currentFunction = module.getFunction(identifier);
        assert currentFunction != null;
        currentBB = currentFunction.getHeadBB();

        node.getStatement().accept(this);

        currentBB.addInstAtTail(new Br(currentBB, null, currentFunction.getReturnBB(), null));

        if (identifier.equals("main")) {
            IR.Function function = module.getFunction("_MxProgramInitial");
            currentFunction.getHeadBB().addInstAtHead(new Call(currentFunction.getHeadBB(), null, function, new ArrayList<>()));
        }

        currentFunction = null;
        currentBB = null;
        return null;
    }

    @Override
    public Object visit(ClassDef node) {
        if (node.getConstructor() != null) {
            node.getConstructor().accept(this);
        }
        for (Function function: node.getFunctions()) {
            function.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(PrimitiveType node) { //Done
        return null;
    }

    @Override
    public Object visit(ClassType node) { //Done
        return null;
    }

    @Override
    public Object visit(ArrayType node) { //Done
        return null;
    }

    @Override
    public Object visit(BlockStmt node) { //Maybe Done
        for (StatementNode statementNode: node.getStatements()) {
            statementNode.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(VarDefStmt node) { //Maybe Done
        for (Variable variable: node.getVariables()) {
            variable.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(IfStmt node) {
        BasicBlock thenBB = new BasicBlock("if_then_block", currentFunction);
        BasicBlock elseBB = new BasicBlock("if_else_block", currentFunction);
        BasicBlock ifExitBB = new BasicBlock("if_exit_block", currentFunction);

        Object condResult = node.getCondition().accept(this);
        assert condResult instanceof ExprResultPair;
        IROper cond = ((ExprResultPair) condResult).result;

        if (node.getElseBody() != null) {
            currentBB.addInstAtTail(new Br(currentBB, cond, thenBB, elseBB));
        } else {
            currentBB.addInstAtTail(new Br(currentBB, cond, thenBB, ifExitBB));
        }

        currentBB = thenBB;
        if (node.getThenBody() != null) {
            node.getThenBody().accept(this);
        }
        currentBB.addInstAtTail(new Br(currentBB, null, ifExitBB, null));
        currentFunction.CheckAndSetName(thenBB.getName(), thenBB);
        currentFunction.addBasicBlock(thenBB);

        if (node.getElseBody() != null) {
            currentBB = elseBB;
            node.getElseBody().accept(this);
            currentBB.addInstAtTail(new Br(currentBB, null, ifExitBB, null));
            currentFunction.CheckAndSetName(elseBB.getName(), elseBB);
            currentFunction.addBasicBlock(elseBB);
        }

        currentBB = ifExitBB;

        currentFunction.CheckAndSetName(ifExitBB.getName(), ifExitBB);
        currentFunction.addBasicBlock(ifExitBB);

        return null;
    }

    @Override
    public Object visit(WhileStmt node) {
        BasicBlock conditionBlock = new BasicBlock("condition_block", currentFunction);
        BasicBlock exitBlock = new BasicBlock("exit_block", currentFunction);
        BasicBlock loopBody = new BasicBlock("loop_body", currentFunction);

        loopContinueBlocks.push(conditionBlock);
        loopExitBlocks.push(exitBlock);

        currentBB.addInstAtTail(new Br(currentBB, null, conditionBlock, null));

        currentBB = conditionBlock;
        Object condResult = node.getExpr().accept(this);
        assert condResult instanceof ExprResultPair;
        currentBB.addInstAtTail(new Br(currentBB, ((ExprResultPair) condResult).result, loopBody, exitBlock));
        currentFunction.CheckAndSetName(conditionBlock.getName(), conditionBlock);
        currentFunction.addBasicBlock(conditionBlock);

        currentBB = loopBody;
        if (node.getBody() != null) {
            node.getBody().accept(this);
        }
        currentBB.addInstAtTail(new Br(currentBB, null, conditionBlock, null));
        currentFunction.CheckAndSetName(loopBody.getName(), loopBody);
        currentFunction.addBasicBlock(loopBody);

        loopContinueBlocks.pop();
        loopExitBlocks.pop();

        currentBB = exitBlock;
        currentFunction.CheckAndSetName(currentBB.getName(), currentBB);
        currentFunction.addBasicBlock(exitBlock);
        return null;
    }

    @Override
    public Object visit(BreakStmt node) {
        currentBB.addInstAtTail(new Br(currentBB, null, loopExitBlocks.peek(), null));
        return null;
    }

    @Override
    public Object visit(ContinueStmt node) {
        currentBB.addInstAtTail(new Br(currentBB, null, loopContinueBlocks.peek(), null));
        return null;
    }

    @Override
    public Object visit(ReturnStmt node) { //Maybe Done
        if (node.getExpr() != null) {
            Object result = node.getExpr().accept(this);
            assert result instanceof ExprResultPair;
            currentBB.addInstAtTail(new Store(currentBB, ((ExprResultPair) result).getResult(), currentFunction.getReturnValue()));
        }
        currentBB.addInstAtTail(new Br(currentBB, null, currentFunction.getReturnBB(), null));
        return null;
    }

    @Override
    public Object visit(ExprStmt node) { //Maybe Done
        return node.getExpr().accept(this);
    }

    @Override
    public Object visit(ForStmt node) {
        BasicBlock conditionBlock = new BasicBlock("condition_block", currentFunction);
        BasicBlock stepBlock = new BasicBlock("step_block", currentFunction);
        BasicBlock loopBody = new BasicBlock("loop_body", currentFunction);
        BasicBlock exitBlock = new BasicBlock("exit_block", currentFunction);

        if (node.getInit() != null) {
            node.getInit().accept(this);
        }

        if ((node.getCond() != null) && (node.getStep() != null)) {
            currentBB.addInstAtTail(new Br(currentBB, null, conditionBlock, null));
            currentBB = conditionBlock;
            Object conditionResult = node.getCond().accept(this);
            assert conditionResult instanceof ExprResultPair;
            currentBB.addInstAtTail(new Br(currentBB, ((ExprResultPair) conditionResult).result, loopBody, exitBlock));
            currentFunction.CheckAndSetName(conditionBlock.getName(), conditionBlock);
            currentFunction.addBasicBlock(conditionBlock);

            loopContinueBlocks.push(stepBlock);
            loopExitBlocks.push(exitBlock);

            currentBB = loopBody;
            if (node.getStatement() != null) {
                node.getStatement().accept(this);
            }
            currentBB.addInstAtTail(new Br(currentBB, null, stepBlock, null));
            currentFunction.CheckAndSetName(loopBody.getName(), loopBody);
            currentFunction.addBasicBlock(loopBody);

            loopExitBlocks.pop();
            loopContinueBlocks.pop();

            currentBB = stepBlock;
            node.getStep().accept(this);
            currentBB.addInstAtTail(new Br(currentBB, null, conditionBlock, null));
            currentFunction.CheckAndSetName(stepBlock.getName(), stepBlock);
            currentFunction.addBasicBlock(stepBlock);
        } else if (node.getCond() != null) {
            currentBB.addInstAtTail(new Br(currentBB, null, conditionBlock, null));

            currentBB = conditionBlock;
            Object conditionResult = node.getCond().accept(this);
            assert conditionResult instanceof ExprResultPair;
            currentBB.addInstAtTail(new Br(currentBB, ((ExprResultPair) conditionResult).result, loopBody, exitBlock));
            currentFunction.CheckAndSetName(conditionBlock.getName(), conditionBlock);
            currentFunction.addBasicBlock(conditionBlock);

            loopContinueBlocks.push(conditionBlock);
            loopExitBlocks.push(exitBlock);

            currentBB = loopBody;
            if (node.getStatement() != null) {
                node.getStatement().accept(this);
            }
            currentBB.addInstAtTail(new Br(currentBB, null, conditionBlock, null));
            currentFunction.CheckAndSetName(loopBody.getName(), loopBody);
            currentFunction.addBasicBlock(loopBody);

            loopExitBlocks.pop();
            loopContinueBlocks.pop();

        } else if (node.getStep() != null) {
            currentBB.addInstAtTail(new Br(currentBB, null, loopBody, null));

            loopContinueBlocks.push(stepBlock);
            loopExitBlocks.push(exitBlock);

            currentBB = loopBody;
            if (node.getStatement() != null) {
                node.getStatement().accept(this);
            }
            currentBB.addInstAtTail(new Br(currentBB, null, stepBlock, null));
            currentFunction.CheckAndSetName(loopBody.getName(), loopBody);
            currentFunction.addBasicBlock(loopBody);

            loopExitBlocks.pop();
            loopContinueBlocks.pop();

            currentBB = stepBlock;
            node.getStep().accept(this);
            currentBB.addInstAtTail(new Br(currentBB, null, loopBody, null));
            currentFunction.CheckAndSetName(stepBlock.getName(), stepBlock);
            currentFunction.addBasicBlock(stepBlock);
        } else {
            currentBB.addInstAtTail(new Br(currentBB, null, loopBody, null));

            loopContinueBlocks.push(loopBody);
            loopExitBlocks.push(exitBlock);

            currentBB = loopBody;
            if (node.getStatement() != null) {
                node.getStatement().accept(this);
            }
            currentBB.addInstAtTail(new Br(currentBB, null, loopBody, null));
            currentFunction.CheckAndSetName(loopBody.getName(), loopBody);
            currentFunction.addBasicBlock(loopBody);

            loopExitBlocks.pop();
            loopContinueBlocks.pop();
        }

        currentBB = exitBlock;
        currentFunction.CheckAndSetName(exitBlock.getName(), exitBlock);
        currentFunction.addBasicBlock(exitBlock);

        return null;
    }

    @Override
    public Object visit(PostFixExpr node) {
        Object exprResult = node.getExpr().accept(this);
        assert exprResult instanceof ExprResultPair;

        Register result;
        if (node.getOp().equals(PostFixExpr.Operator.postFixIncrease)) {
            result = new Register(new IntegerType(32),
                    ((Register) ((ExprResultPair) exprResult).getResult()).getName() + "post_fix_increase__");
            currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.add,
                    new IntegerType(32), ((ExprResultPair) exprResult).result, new IntegerConstant(1)));
        } else {
            assert node.getOp().equals(PostFixExpr.Operator.postFixDecrease);
            result = new Register(new IntegerType(32),
                    ((Register) ((ExprResultPair) exprResult).getResult()).getName() + "post_fix_decrease__");
            currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.sub,
                    new IntegerType(32), ((ExprResultPair) exprResult).result, new IntegerConstant(1)));
        }
        currentBB.addInstAtTail(new Store(currentBB, result, ((ExprResultPair) exprResult).addr));
        currentFunction.CheckAndSetName(result.getName(), result);
        return new ExprResultPair(((ExprResultPair) exprResult).result, null);
    }

    private Register newArray(Deque<IROper> sizePerDim, IRType irType) {
        assert irType instanceof PointerType;

        ArrayList<IROper> paramForMalloc = new ArrayList<>();
        IntegerConstant typeSize = new IntegerConstant(((PointerType) irType).getType().getByte());
        Register arraySize = new Register(new IntegerType(32), "arraySize");
        currentBB.addInstAtTail(new BinaryOperation(currentBB, arraySize, BinaryOperation.BinaryOp.mul,
                new IntegerType(32), typeSize, sizePerDim.getFirst()));
        currentFunction.CheckAndSetName(arraySize.getName(), arraySize);
        Register arraySizeWithTag = new Register(new IntegerType(32), "arraySizeWithTag");
        currentBB.addInstAtTail(new BinaryOperation(currentBB, arraySizeWithTag, BinaryOperation.BinaryOp.add,
                new IntegerType(32), arraySize, new IntegerConstant(4)));
        paramForMalloc.add(arraySizeWithTag);
        currentFunction.CheckAndSetName(arraySizeWithTag.getName(), arraySizeWithTag);

//        Register mallocResult = new Register(new PointerType(new IntegerType(8)), "mallocResult");
//        IR.Function mallocFunction = module.getFunction("malloc");
//        currentBB.addInstAtTail(new Call(currentBB, mallocResult, mallocFunction, paramForMalloc));
//        currentFunction.CheckAndSetName(mallocResult.getName(), mallocResult);
//        Register mallocI32Ptr = new Register(new PointerType(new IntegerType(32)), "mallocI32Ptr");
//        currentBB.addInstAtTail(new BitCastTo(currentBB, mallocI32Ptr, mallocResult, new PointerType(new IntegerType(32))));
//        currentFunction.CheckAndSetName(mallocI32Ptr.getName(), mallocI32Ptr);
//        Register mallocResult = new Register(new PointerType(new IntegerType(8)), "mallocResult");
        IR.Function mallocFunction = module.getFunction("malloc");
        Register mallocI32Ptr = new Register(new PointerType(new IntegerType(32)), "mallocI32Ptr");
        currentBB.addInstAtTail(new Call(currentBB, mallocI32Ptr, mallocFunction, paramForMalloc));
//        currentFunction.CheckAndSetName(mallocResult.getName(), mallocResult);
//        currentBB.addInstAtTail(new BitCastTo(currentBB, mallocI32Ptr, mallocResult, new PointerType(new IntegerType(32))));
        currentFunction.CheckAndSetName(mallocI32Ptr.getName(), mallocI32Ptr);

        currentBB.addInstAtTail(new Store(currentBB, sizePerDim.getFirst(), mallocI32Ptr));

        Register headI32Ptr = new Register(new PointerType(new IntegerType(32)), "headI32Ptr");
        ArrayList<IROper> index = new ArrayList<>();
        index.add(new IntegerConstant(1));
        currentBB.addInstAtTail(new GetElementPtr(currentBB, headI32Ptr, mallocI32Ptr, index));
        currentFunction.CheckAndSetName(headI32Ptr.getName(), headI32Ptr);

        Register headPtr = new Register(irType, "headPtr");
        currentBB.addInstAtTail(new BitCastTo(currentBB, headPtr, headI32Ptr, headPtr.getType()));
        currentFunction.CheckAndSetName(headPtr.getName(), headPtr);

        Deque<IROper> sizePerDimForNext = new ArrayDeque<>(sizePerDim);
        sizePerDimForNext.removeFirst();

        if (sizePerDimForNext.isEmpty()) {
            return headPtr;
        }


        ArrayList<IROper> paramForMallocIterator = new ArrayList<>();
        IntegerConstant iterator = new IntegerConstant(0);
        IntegerConstant size = new IntegerConstant(iterator.getType().getByte());
        paramForMallocIterator.add(size);
        Register iteratorAddr = new Register(new PointerType(new IntegerType(8)), "iteratorAddr");
        currentFunction.CheckAndSetName(iteratorAddr.getName(), iteratorAddr);
        Register Addr = new Register(new PointerType(new IntegerType(32)), "iterator_addr");
        currentFunction.CheckAndSetName(Addr.getName(), Addr);
//        currentFunction.getHeadBB().addInstAtHead(new Store(currentFunction.getHeadBB(), new IntegerConstant(0), iteratorAddr));
//        currentFunction.getHeadBB().addInstAtHead(new Alloca(currentFunction.getHeadBB(), iteratorAddr, new IntegerType(32)));
        currentFunction.getHeadBB().addInstAtHead(new Store(currentFunction.getHeadBB(), new IntegerConstant(0), Addr));
        currentFunction.getHeadBB().addInstAtHead(new BitCastTo(currentFunction.getHeadBB(), Addr,
                iteratorAddr, new PointerType(new IntegerType(32))));
        currentFunction.getHeadBB().addInstAtHead(new Call(currentFunction.getHeadBB(), iteratorAddr,
                mallocFunction, paramForMallocIterator));
        currentBB.addInstAtTail(new Store(currentBB, iterator, Addr));


        BasicBlock condBB = new BasicBlock("condBB", currentFunction);
        BasicBlock bodyBB = new BasicBlock("bodyBB", currentFunction);
        BasicBlock exitBB = new BasicBlock("exitBB", currentFunction);
        currentFunction.CheckAndSetName(condBB.getName(), condBB);
        currentFunction.CheckAndSetName(bodyBB.getName(), bodyBB);
        currentFunction.CheckAndSetName(exitBB.getName(), exitBB);

        currentBB.addInstAtTail(new Br(currentBB, null, condBB, null));
        currentBB = condBB;
        currentFunction.addBasicBlock(condBB);
        Register curIterator = new Register(new IntegerType(32), "curIterator");
        currentBB.addInstAtTail(new Load(currentBB, curIterator, new IntegerType(32), Addr));
        currentFunction.CheckAndSetName(curIterator.getName(), curIterator);
        Register cond = new Register(new IntegerType(1), "cond");
        currentBB.addInstAtTail(new Icmp(currentBB, cond, Icmp.Condition.slt, new IntegerType(32), curIterator, sizePerDim.getFirst()));
        currentFunction.CheckAndSetName(cond.getName(), cond);
        currentBB.addInstAtTail(new Br(currentBB, cond, bodyBB, exitBB));

        currentBB = bodyBB;
        currentFunction.addBasicBlock(bodyBB);
        Register headPtrOfNextDim = newArray(sizePerDimForNext, ((PointerType) irType).getType());
        Register curPtr = new Register(irType, "curPtr");
        ArrayList<IROper> curParam = new ArrayList<>();
        curParam.add(curIterator);
        currentBB.addInstAtTail(new GetElementPtr(currentBB, curPtr, headPtr, curParam));
        currentBB.addInstAtTail(new Store(currentBB, headPtrOfNextDim, curPtr));
        currentFunction.CheckAndSetName(curPtr.getName(), curPtr);

        Register nextIterator = new Register(new IntegerType(32), "nextIterator");
        currentBB.addInstAtTail(new BinaryOperation(currentBB, nextIterator, BinaryOperation.BinaryOp.add,
                new IntegerType(32), curIterator, new IntegerConstant(1)));
        currentFunction.CheckAndSetName(nextIterator.getName(), nextIterator);
        currentBB.addInstAtTail(new Store(currentBB, nextIterator, Addr));

        currentBB.addInstAtTail(new Br(currentBB, null, condBB, null));

        currentBB = exitBB;
        currentFunction.addBasicBlock(exitBB);

        return headPtr;
    }

    @Override
    public Object visit(NewExpr node) {
        if (node.getDim() != 0) {
            IRType baseType = module.getIrTypeTable().get(astTypeTable.getType2(node.getType()));
            if (baseType instanceof StructureType) {
                baseType = new PointerType(baseType);
            }
            for (int i = 0; i < node.getDim(); ++i) {
                baseType = new PointerType(baseType);
            }

            ArrayList<ExprNode> exprPerDim = node.getExprPerDim();
            Deque<IROper> sizePerDim = new ArrayDeque<>();
            for (ExprNode exprNode: exprPerDim) {
                Object sizeResult = exprNode.accept(this);
                assert sizeResult instanceof ExprResultPair;
                sizePerDim.addLast(((ExprResultPair) sizeResult).result);
            }
            Register result = newArray(sizePerDim, baseType);
            return new ExprResultPair(result, null);
        } else {
            Type2 type2 = astTypeTable.getType2(node.getType());
            assert type2 instanceof ClassType2;
            IRType structureType = module.getIrTypeTable().get(type2);
            int structureSize = structureType.getByte();
            assert structureType instanceof StructureType;
            structureType = new PointerType(structureType);

//            ArrayList<IROper> paramForMalloc = new ArrayList<>();
//            paramForMalloc.add(new IntegerConstant(structureSize));
//            Register mallocResult = new Register(new PointerType(new IntegerType(8)), "mallocResult");
//            Register addr = new Register(structureType, "classAddr");
//            IR.Function mallocFunction = module.getFunction("malloc");
//            currentBB.addInstAtTail(new Call(currentBB, mallocResult, mallocFunction, paramForMalloc));
//            currentFunction.CheckAndSetName(mallocResult.getName(), mallocResult);
//            currentBB.addInstAtTail(new BitCastTo(currentBB, addr, mallocResult, structureType));
//            currentFunction.CheckAndSetName(addr.getName(), addr);

            ArrayList<IROper> paramForMalloc = new ArrayList<>();
            paramForMalloc.add(new IntegerConstant(structureSize));
//            Register mallocResult = new Register(new PointerType(new IntegerType(8)), "mallocResult");
            Register addr = new Register(structureType, "classAddr");
            IR.Function mallocFunction = module.getFunction("malloc");
            currentBB.addInstAtTail(new Call(currentBB, addr, mallocFunction, paramForMalloc));
//            currentFunction.CheckAndSetName(mallocResult.getName(), mallocResult);
//            currentBB.addInstAtTail(new BitCastTo(currentBB, addr, mallocResult, structureType));
            currentFunction.CheckAndSetName(addr.getName(), addr);

            if (((ClassType2) type2).getConstructor() != null) {
                IR.Function constructor = module.getFunction(type2.getTypeName() + "." + ((ClassType2) type2).getConstructor().getName());
                ArrayList<IROper> paramForConstructor = new ArrayList<>();
                paramForConstructor.add(addr);
                currentBB.addInstAtTail(new Call(currentBB, null, constructor, paramForConstructor));
            }

            return new ExprResultPair(addr, null);
        }
    }

    @Override
    public Object visit(MemberExpr node) {
        Object baseClassResult =  node.getExpr().accept(this);
        assert baseClassResult instanceof ExprResultPair;
        IROper resultPointer = ((ExprResultPair) baseClassResult).result;
        Type2 baseClassType = node.getExpr().getType2();
        assert baseClassType instanceof ClassType2;

        String identifier = node.getIdentifier();
        ArrayList<VariableEntity> memberList = ((ClassType2) baseClassType).getMembers();
        AtomicInteger index = new AtomicInteger();
        TypeNode type = null;
        for (VariableEntity member: memberList) {
            if (member.getName().equals(identifier)) {
                type = member.getType();
                break;
            }
            index.getAndIncrement();
        }
        assert type != null;
        IRType memberType = module.getIrTypeTable().get(astTypeTable.getType2(type));
        if (memberType instanceof StructureType) {
            memberType = new PointerType(memberType);
        }
        ArrayList<IROper> indexList = new ArrayList<>();
        indexList.add(new IntegerConstant(0));
        indexList.add(new IntegerConstant(index.longValue()));
        Register result = new Register(new PointerType(memberType), baseClassType.getTypeName() + "." + identifier + "_pointer");
        currentBB.addInstAtTail(new GetElementPtr(currentBB, result, resultPointer, indexList));
        Register loadResult = new Register(memberType, baseClassType.getTypeName() + "." + identifier);
        currentBB.addInstAtTail(new Load(currentBB, loadResult, memberType, result));
        currentFunction.CheckAndSetName(result.getName(), result);
        currentFunction.CheckAndSetName(loadResult.getName(), loadResult);
        return new ExprResultPair(loadResult, result);
    }

    @Override
    public Object visit(FuncCallExpr node) {
        ExprNode funcExpr = node.getFuncExpr();
        IR.Function function;
        if (funcExpr instanceof MemberExpr) {
            Object baseTypeResult = ((MemberExpr) funcExpr).getExpr().accept(this);
            assert baseTypeResult instanceof ExprResultPair;
            String identifier = ((MemberExpr) funcExpr).getIdentifier();
            Type2 baseType = ((MemberExpr) funcExpr).getExpr().getType2();
            if (baseType instanceof ClassType2) {
                function = module.getFunction(baseType.getTypeName() + "." + identifier); //maybe builtin function, to solve
                assert function != null;
                IRType returnType = function.getFunctionType().getReturnType();
                Register callResult = returnType instanceof VoidType ? null : new Register(returnType, "call_class_method");
                ArrayList<IROper> params = new ArrayList<>();
                params.add(((ExprResultPair) baseTypeResult).result);
                for (ExprNode param : node.getExprNodes()) {
                    Object paramResult = param.accept(this);
                    assert paramResult instanceof ExprResultPair;
                    params.add(((ExprResultPair) paramResult).result);
                }
                currentBB.addInstAtTail(new Call(currentBB, callResult, function, params));
                if (callResult != null) {
                    currentFunction.CheckAndSetName(callResult.getName(), callResult);
                }
                return new ExprResultPair(callResult, null);
            } else if (baseType instanceof StringType2) {
                function = module.getFunction("_string_" + identifier);
                assert function != null;
                IRType returnType = function.getFunctionType().getReturnType();
                Register callResult = returnType instanceof VoidType ? null : new Register(returnType, "call_string_method");
                ArrayList<IROper> params = new ArrayList<>();
                params.add(((ExprResultPair) baseTypeResult).result);
                for (ExprNode param : node.getExprNodes()) {
                    Object paramResult = param.accept(this);
                    assert paramResult instanceof ExprResultPair;
                    params.add(((ExprResultPair) paramResult).result);
                }
                currentBB.addInstAtTail(new Call(currentBB, callResult, function, params));
                if (callResult != null) {
                    currentFunction.CheckAndSetName(callResult.getName(), callResult);
                }
                return new ExprResultPair(callResult, null);
            } else if (baseType instanceof ArrayType2) {
                Register pointer = new Register(new PointerType(new IntegerType(32)), "for_getting_size");
                currentBB.addInstAtTail(new BitCastTo(currentBB, pointer, ((ExprResultPair) baseTypeResult).result, new PointerType(new IntegerType(32))));
                currentFunction.CheckAndSetName(pointer.getName(), pointer);

                ArrayList<IROper> index = new ArrayList<>();
                index.add(new IntegerConstant(-1));
                Register sizeResultPointer = new Register(new PointerType(new IntegerType(32)), "size_result_pointer");
                currentBB.addInstAtTail(new GetElementPtr(currentBB, sizeResultPointer, pointer, index));
                Register sizeResult = new Register(new IntegerType(32), "size_result");
                currentBB.addInstAtTail(new Load(currentBB, sizeResult, new IntegerType(32), sizeResultPointer));
                currentFunction.CheckAndSetName(sizeResultPointer.getName(), sizeResultPointer);
                currentFunction.CheckAndSetName(sizeResult.getName(), sizeResult);

                return new ExprResultPair(sizeResult, null);
            }
        } else {
            assert funcExpr instanceof IdentifierExpr;
            String identifier = ((IdentifierExpr) funcExpr).getIdentifier();
            if (!node.getScope().IsMethod(identifier)) {
                function = module.getFunction(identifier);
                assert function != null;
                IRType returnType = function.getFunctionType().getReturnType();
                Register callResult = returnType instanceof VoidType ? null : new Register(returnType, "call_function");
                ArrayList<IROper> params = new ArrayList<>();
                for (ExprNode param : node.getExprNodes()) {
                    Object paramResult = param.accept(this);
                    assert paramResult instanceof ExprResultPair;
                    params.add(((ExprResultPair) paramResult).result);
                }
                currentBB.addInstAtTail(new Call(currentBB, callResult, function, params));
                if (callResult != null) {
                    currentFunction.CheckAndSetName(callResult.getName(), callResult);
                }
                return new ExprResultPair(callResult, null);
            } else {
                Type2 baseType = node.getScope().getClassType();
                function = module.getFunction(baseType.getTypeName() + "." + identifier);
                assert function != null;

                Register thisAddr = (Register) currentFunction.getOperand(baseType.getTypeName() + ".this");
                assert thisAddr.getType() instanceof PointerType;
                IRType type = ((PointerType) thisAddr.getType()).getType();
                Register thisResult = new Register(type, "this");
                currentBB.addInstAtTail(new Load(currentBB, thisResult, type, thisAddr));
                currentFunction.CheckAndSetName(thisResult.getName(), thisResult);

                IRType returnType = function.getFunctionType().getReturnType();
                Register callResult = returnType instanceof VoidType ? null : new Register(returnType, "call_class_method");
                ArrayList<IROper> params = new ArrayList<>();
                params.add(thisResult);
                for (ExprNode param : node.getExprNodes()) {
                    Object paramResult = param.accept(this);
                    assert paramResult instanceof ExprResultPair;
                    params.add(((ExprResultPair) paramResult).result);
                }
                currentBB.addInstAtTail(new Call(currentBB, callResult, function, params));
                if (callResult != null) {
                    currentFunction.CheckAndSetName(callResult.getName(), callResult);
                }
                return new ExprResultPair(callResult, null);
            }


        }
        return null;
    }

    @Override
    public Object visit(SubScriptExpr node) {
        Object nameExprResult = node.getNameExpr().accept(this);
        assert nameExprResult instanceof ExprResultPair;
        Object dimExprResult =  node.getDimExpr().accept(this);
        assert dimExprResult instanceof ExprResultPair;

        Register resultAddr = new Register(((ExprResultPair) nameExprResult).getResult().getType(), "__element_pointer__");
        ArrayList<IROper> idxes = new ArrayList<>();
        idxes.add(((ExprResultPair) dimExprResult).result);
        currentBB.addInstAtTail(new GetElementPtr(currentBB, resultAddr, ((ExprResultPair) nameExprResult).result, idxes));
        currentFunction.CheckAndSetName(resultAddr.getName(), resultAddr);
        Register arrayReg = new Register(((PointerType)(((ExprResultPair) nameExprResult).getResult().getType())).getType(), "__array_register__");
        currentBB.addInstAtTail(new Load(currentBB, arrayReg, arrayReg.getType(), resultAddr));
        currentFunction.CheckAndSetName(arrayReg.getName(), arrayReg);
        return new ExprResultPair(arrayReg, resultAddr);
    }

    @Override
    public Object visit(PreFixExpr node) {
        Object exprResult = node.getExpr().accept(this);
        assert exprResult instanceof ExprResultPair;
        Register exprResultReg;
        IROper exprResultAddr = ((ExprResultPair) exprResult).addr;

        Register result;
        switch (node.getOp()) {
            case preFixIncrease:
                exprResultReg = (Register) ((ExprResultPair) exprResult).result;
                result = new Register(new IntegerType(32), exprResultReg.getName() + "__pre_fix_increase__");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.add,
                        new IntegerType(32), exprResultReg, new IntegerConstant(1)));
                currentBB.addInstAtTail(new Store(currentBB, result, exprResultAddr));
                currentFunction.CheckAndSetName(result.getName(), result);
                return new ExprResultPair(result, exprResultAddr);
            case preFixDecrease:
                exprResultReg = (Register) ((ExprResultPair) exprResult).result;
                result = new Register(new IntegerType(32), exprResultReg.getName() + "__pre_fix_decrease__");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.sub,
                        new IntegerType(32), exprResultReg, new IntegerConstant(1)));
                currentBB.addInstAtTail(new Store(currentBB, result, exprResultAddr));
                currentFunction.CheckAndSetName(result.getName(), result);
                return new ExprResultPair(result, exprResultAddr);
            case preFixPlus:
                return new ExprResultPair(((ExprResultPair) exprResult).getResult(), null);
            case preFixSub:
                if (((ExprResultPair) exprResult).result instanceof IntegerConstant) {
                    return new ExprResultPair(new IntegerConstant(-((IntegerConstant) ((ExprResultPair) exprResult).result).getValue()), null);
                } else {
                    assert ((ExprResultPair) exprResult).result instanceof Register;
                    exprResultReg = (Register) ((ExprResultPair) exprResult).result;
                    result = new Register(new IntegerType(32), exprResultReg.getName() + "__pre_fix_sub__");
                    currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.sub,
                            new IntegerType(32), new IntegerConstant(0), exprResultReg));
                    currentFunction.CheckAndSetName(result.getName(),result);
                    return new ExprResultPair(result, exprResultAddr);
                }
            case negation:
                exprResultReg = (Register) ((ExprResultPair) exprResult).result;
                result = new Register(new IntegerType(1), exprResultReg.getName() + "__negation__");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.xor,
                        new IntegerType(1), exprResultReg, new BoolConstant(true)));
                currentFunction.CheckAndSetName(result.getName(), result);
                return new ExprResultPair(result, exprResultAddr);
            case bitwiseComplement:
                exprResultReg = (Register) ((ExprResultPair) exprResult).result;
                result = new Register(new IntegerType(1), exprResultReg.getName() + "__bitwiseComplement__");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.xor,
                        new IntegerType(-1), exprResultReg, new BoolConstant(true)));
                currentFunction.CheckAndSetName(result.getName(), result);
                return new ExprResultPair(result, exprResultAddr);
        }
        return null;
    }

    @Override
    public Object visit(BinaryExpr node) {
        Object opd1Result;
        Object opd2Result;
        Register opd1ResultReg;
//        Register opd2ResultReg;
        IROper opd1ResultAddr;
        IROper opd2ResultAddr;
        Register result;

        Register resultAddr;

        BasicBlock opd1BB;
        BasicBlock opd2BB;
        BasicBlock exitBB;

        Set<Pair<BasicBlock, IROper>> possiblePredecessorSet;

        IR.Function function;
        ArrayList<IROper> params;

        Type2 op1Type2 = node.getOpd1().getType2();
        Type2 op2Type2 = node.getOpd2().getType2();

        switch (node.getOp()) {
            case multiply:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                result = new Register(new IntegerType(32), "multiply");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.mul,
                        new IntegerType(32), ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                currentFunction.CheckAndSetName(result.getName(), result);

                return new ExprResultPair(result, null);




            case division:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                result = new Register(new IntegerType(32), "division");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.sdiv,
                        new IntegerType(32), ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                currentFunction.CheckAndSetName(result.getName(), result);

                return new ExprResultPair(result, null);




            case mod:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                result = new Register(new IntegerType(32), "mod");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.srem,
                        new IntegerType(32), ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                currentFunction.CheckAndSetName(result.getName(), result);

                return new ExprResultPair(result, null);




            case plus:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                if ((node.getOpd1().getType2() instanceof IntType2) && (node.getOpd2().getType2() instanceof IntType2)) {
                    result = new Register(new IntegerType(32), "add");
                    currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.add,
                            new IntegerType(32), ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));

                } else {
                    function = module.getFunction("_string_concatenate");
                    result = new Register(new PointerType(new IntegerType(8)), "str_add");
                    params = new ArrayList<>();
                    params.add(((ExprResultPair) opd1Result).result);
                    params.add(((ExprResultPair) opd2Result).result);
                    currentBB.addInstAtTail(new Call(currentBB, result, function, params));
                }
                currentFunction.CheckAndSetName(result.getName(), result);
                return new ExprResultPair(result, null);


            case sub:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                result = new Register(new IntegerType(32), "sub");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.sub,
                        new IntegerType(32), ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                currentFunction.CheckAndSetName(result.getName(), result);

                return new ExprResultPair(result, null);




            case shiftLeft:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                result = new Register(new IntegerType(32), "shiftLeft");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.shl,
                        new IntegerType(32), ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                currentFunction.CheckAndSetName(result.getName(), result);

                return new ExprResultPair(result, null);




            case shiftRight:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                result = new Register(new IntegerType(32), "shiftRight");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.ashr,
                        new IntegerType(32), ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                currentFunction.CheckAndSetName(result.getName(), result);

                return new ExprResultPair(result, null);




            case less:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                if ((node.getOpd1().getType2() instanceof IntType2) && (node.getOpd2().getType2() instanceof IntType2)) {
                    result = new Register(new IntegerType(1), "less");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.slt, new IntegerType(32),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));

                } else {
                    function = module.getFunction("_string_less");
                    result = new Register(new IntegerType(1), "str_less");
                    params = new ArrayList<>();
                    params.add(((ExprResultPair) opd1Result).result);
                    params.add(((ExprResultPair) opd2Result).result);
                    currentBB.addInstAtTail(new Call(currentBB, result, function, params));
                }
                currentFunction.CheckAndSetName(result.getName(), result);
                return new ExprResultPair(result, null);




            case greater:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                if ((node.getOpd1().getType2() instanceof IntType2) && (node.getOpd2().getType2() instanceof IntType2)) {
                    result = new Register(new IntegerType(1), "greater");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.sgt, new IntegerType(32),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));

                } else {
                    function = module.getFunction("_string_greater");
                    result = new Register(new IntegerType(1), "str_greater");
                    params = new ArrayList<>();
                    params.add(((ExprResultPair) opd1Result).result);
                    params.add(((ExprResultPair) opd2Result).result);
                    currentBB.addInstAtTail(new Call(currentBB, result, function, params));
                }
                currentFunction.CheckAndSetName(result.getName(), result);
                return new ExprResultPair(result, null);




            case lessEqual:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                if ((node.getOpd1().getType2() instanceof IntType2) && (node.getOpd2().getType2() instanceof IntType2)) {
                    result = new Register(new IntegerType(1), "lessEqual");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.sle, new IntegerType(32),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));

                } else {
                    function = module.getFunction("_string_lessEqual");
                    result = new Register(new IntegerType(1), "str_lessEqual");
                    params = new ArrayList<>();
                    params.add(((ExprResultPair) opd1Result).result);
                    params.add(((ExprResultPair) opd2Result).result);
                    currentBB.addInstAtTail(new Call(currentBB, result, function, params));
                }
                currentFunction.CheckAndSetName(result.getName(), result);
                return new ExprResultPair(result, null);




            case greaterEqual:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                if ((node.getOpd1().getType2() instanceof IntType2) && (node.getOpd2().getType2() instanceof IntType2)) {
                    result = new Register(new IntegerType(1), "greaterEqual");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.sge, new IntegerType(32),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));

                } else {
                    function = module.getFunction("_string_greaterEqual");
                    result = new Register(new IntegerType(1), "str_greaterEqual");
                    params = new ArrayList<>();
                    params.add(((ExprResultPair) opd1Result).result);
                    params.add(((ExprResultPair) opd2Result).result);
                    currentBB.addInstAtTail(new Call(currentBB, result, function, params));
                }
                currentFunction.CheckAndSetName(result.getName(), result);
                return new ExprResultPair(result, null);




            case equal:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;


                if ((op1Type2 instanceof IntType2) && (op2Type2 instanceof IntType2)) {
                    result = new Register(new IntegerType(1), "equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.eq, new IntegerType(32),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                } else if ((op1Type2 instanceof BoolType2) && (op2Type2 instanceof BoolType2)) {
                    result = new Register(new IntegerType(1), "equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.eq, new IntegerType(1),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                } else if ((op1Type2 instanceof ArrayType2) && (op2Type2 instanceof NullType2)) {
                    result = new Register(new IntegerType(1), "equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.eq, ((ExprResultPair) opd1Result).result.getType(),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                } else if ((op1Type2 instanceof NullType2) && (op2Type2 instanceof ArrayType2)) {
                    result = new Register(new IntegerType(1), "equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.eq, ((ExprResultPair) opd2Result).result.getType(),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                } else if ((op1Type2 instanceof ClassType2) && (op2Type2 instanceof NullType2)) {
                    result = new Register(new IntegerType(1), "equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.eq, ((ExprResultPair) opd1Result).result.getType(),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                } else if ((op1Type2 instanceof NullType2) && (op2Type2 instanceof ClassType2)) {
                    result = new Register(new IntegerType(1), "equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.eq, ((ExprResultPair) opd2Result).result.getType(),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                }  else if ((op1Type2 instanceof NullType2) && (op2Type2 instanceof NullType2)) {
                    result = new Register(new IntegerType(1), "equal");
                    currentBB.addInstAtTail(new Store(currentBB, new BoolConstant(true), result));
                } else {
                    function = module.getFunction("_string_equal");
                    result = new Register(new IntegerType(1), "str_equal");
                    params = new ArrayList<>();
                    params.add(((ExprResultPair) opd1Result).result);
                    params.add(((ExprResultPair) opd2Result).result);
                    currentBB.addInstAtTail(new Call(currentBB, result, function, params));
                }
                currentFunction.CheckAndSetName(result.getName(), result);
                return new ExprResultPair(result, null);




            case notEqual:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                if ((op1Type2 instanceof IntType2) && (op2Type2 instanceof IntType2)) {
                    result = new Register(new IntegerType(1), "not_equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.ne, new IntegerType(32),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                } else if ((op1Type2 instanceof BoolType2) && (op2Type2 instanceof BoolType2)) {
                    result = new Register(new IntegerType(1), "not_equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.ne, new IntegerType(1),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                } else if ((op1Type2 instanceof ArrayType2) && (op2Type2 instanceof NullType2)) {
                    result = new Register(new IntegerType(1), "not_equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.ne, ((ExprResultPair) opd1Result).result.getType(),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                } else if ((op1Type2 instanceof NullType2) && (op2Type2 instanceof ArrayType2)) {
                    result = new Register(new IntegerType(1), "not_equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.ne, ((ExprResultPair) opd2Result).result.getType(),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                } else if ((op1Type2 instanceof ClassType2) && (op2Type2 instanceof NullType2)) {
                    result = new Register(new IntegerType(1), "not_equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.ne, ((ExprResultPair) opd1Result).result.getType(),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                } else if ((op1Type2 instanceof NullType2) && (op2Type2 instanceof ClassType2)) {
                    result = new Register(new IntegerType(1), "not_equal");
                    currentBB.addInstAtTail(new Icmp(currentBB, result, Icmp.Condition.ne, ((ExprResultPair) opd2Result).result.getType(),
                            ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                }  else if ((op1Type2 instanceof NullType2) && (op2Type2 instanceof NullType2)) {
                    result = new Register(new IntegerType(1), "not_equal");
                    currentBB.addInstAtTail(new Store(currentBB, new BoolConstant(false), result));
                } else {
                    function = module.getFunction("_string_notEqual");
                    result = new Register(new IntegerType(1), "str_not_equal");
                    params = new ArrayList<>();
                    params.add(((ExprResultPair) opd1Result).result);
                    params.add(((ExprResultPair) opd2Result).result);
                    currentBB.addInstAtTail(new Call(currentBB, result, function, params));
                }
                currentFunction.CheckAndSetName(result.getName(), result);
                return new ExprResultPair(result, null);




            case bitWiseAnd:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                result = new Register(new IntegerType(32), "bitWiseAnd");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.and,
                        new IntegerType(32), ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                currentFunction.CheckAndSetName(result.getName(), result);

                return new ExprResultPair(result, null);




            case bitWiseOr:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                result = new Register(new IntegerType(32), "bitWiseOr");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.or,
                        new IntegerType(32), ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                currentFunction.CheckAndSetName(result.getName(), result);

                return new ExprResultPair(result, null);




            case bitWiseXor:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                result = new Register(new IntegerType(32), "bitWiseXor");
                currentBB.addInstAtTail(new BinaryOperation(currentBB, result, BinaryOperation.BinaryOp.xor,
                        new IntegerType(32), ((ExprResultPair) opd1Result).result, ((ExprResultPair) opd2Result).result));
                currentFunction.CheckAndSetName(result.getName(), result);

                return new ExprResultPair(result, null);




            case and:
                ArrayList<IROper> paramForAndMalloc = new ArrayList<>();
                paramForAndMalloc.add(new IntegerConstant(1));
                IR.Function andMalloc = module.getFunction("malloc");
//                Register andMallocResult = new Register(new PointerType(new IntegerType(8)), "andMallocResult");
//                currentFunction.CheckAndSetName(andMallocResult.getName(), andMallocResult);
                resultAddr = new Register(new PointerType(new IntegerType(1)), "andResult");
                currentFunction.CheckAndSetName(resultAddr.getName(), resultAddr);
                currentFunction.getHeadBB().addInstAtHead(new Store(currentFunction.getHeadBB(), new BoolConstant(false), resultAddr));
//                currentFunction.getHeadBB().addInstAtHead(new BitCastTo(currentFunction.getHeadBB(), resultAddr, andMallocResult, new PointerType(new IntegerType(1))));
                currentFunction.getHeadBB().addInstAtHead(new Call(currentFunction.getHeadBB(), resultAddr, andMalloc, paramForAndMalloc));

//                ArrayList<IROper> paramForAndMalloc = new ArrayList<>();
//                paramForAndMalloc.add(new IntegerConstant(1));
//                IR.Function andMalloc = module.getFunction("malloc");
//                Register andMallocResult = new Register(new PointerType(new IntegerType(8)), "andMallocResult");
//                currentFunction.CheckAndSetName(andMallocResult.getName(), andMallocResult);
//                resultAddr = new Register(new PointerType(new IntegerType(1)), "andResult");
//                currentFunction.CheckAndSetName(resultAddr.getName(), resultAddr);
//                currentFunction.getHeadBB().addInstAtHead(new Store(currentFunction.getHeadBB(), new BoolConstant(false), resultAddr));
//                currentFunction.getHeadBB().addInstAtHead(new BitCastTo(currentFunction.getHeadBB(), resultAddr, andMallocResult, new PointerType(new IntegerType(1))));
//                currentFunction.getHeadBB().addInstAtHead(new Call(currentFunction.getHeadBB(), andMallocResult, andMalloc, paramForAndMalloc));
//                currentFunction.getHeadBB().addInstAtHead(new Alloca(currentFunction.getHeadBB(), resultAddr, new IntegerType(1)));
                opd2BB = new BasicBlock("opd2BB", currentFunction);
                exitBB = new BasicBlock("exitBB", currentFunction);

                opd1Result = node.getOpd1().accept(this);
                opd1BB = currentBB;
                assert opd1Result instanceof ExprResultPair;
                currentBB.addInstAtTail(new Store(currentBB, ((ExprResultPair) opd1Result).result, resultAddr));
                currentBB.addInstAtTail(new Br(currentBB, ((ExprResultPair) opd1Result).result, opd2BB, exitBB));

                currentBB = opd2BB;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
                currentBB.addInstAtTail(new Store(currentBB, ((ExprResultPair) opd2Result).result, resultAddr));
                currentBB.addInstAtTail(new Br(currentBB, null, exitBB, null));
                currentFunction.addBasicBlock(opd2BB);
                currentFunction.CheckAndSetName(currentBB.getName(), currentBB);

                currentBB = exitBB;
                result = new Register(new IntegerType(1), "and");
                currentBB.addInstAtTail(new Load(currentBB, result, new IntegerType(1), resultAddr));
                currentFunction.addBasicBlock(currentBB);
                currentFunction.CheckAndSetName(result.getName(), result);
                currentFunction.CheckAndSetName(currentBB.getName(), currentBB);

                return new ExprResultPair(result, null);

//                opd2BB = new BasicBlock("opd2BB", currentFunction);
//                exitBB = new BasicBlock("exitBB", currentFunction);
//
//                opd1Result = node.getOpd1().accept(this);
//                opd1BB = currentBB;
//                assert opd1Result instanceof ExprResultPair;
//                currentBB.addInstAtTail(new Br(currentBB, ((ExprResultPair) opd1Result).result, opd2BB, exitBB));
//
//                currentBB = opd2BB;
//                opd2Result = node.getOpd2().accept(this);
//                assert opd2Result instanceof ExprResultPair;
//                currentBB.addInstAtTail(new Br(currentBB, null, exitBB, null));
//                currentFunction.addBasicBlock(opd2BB);
//                currentFunction.CheckAndSetName(currentBB.getName(), currentBB);
//
//                currentBB = exitBB;
//                result = new Register(new IntegerType(1), "and");
//                possiblePredecessorSet = new LinkedHashSet<>();
//                possiblePredecessorSet.add(new Pair<BasicBlock, IROper>(opd1BB, new BoolConstant(false)));
//                possiblePredecessorSet.add(new Pair<BasicBlock, IROper>(opd2BB, ((ExprResultPair) opd2Result).result));
//                currentBB.addInstAtTail(new Phi(currentBB, result, possiblePredecessorSet));
//                currentFunction.addBasicBlock(currentBB);
//                currentFunction.CheckAndSetName(result.getName(), result);
//                currentFunction.CheckAndSetName(currentBB.getName(), currentBB);
//
//                return new ExprResultPair(result, null);




            case or:
                ArrayList<IROper> paramForOrMalloc = new ArrayList<>();
                paramForOrMalloc.add(new IntegerConstant(1));
                IR.Function orMalloc = module.getFunction("malloc");
//                Register orMallocResult = new Register(new PointerType(new IntegerType(8)), "orMallocResult");
//                currentFunction.CheckAndSetName(orMallocResult.getName(), orMallocResult);
                resultAddr = new Register(new PointerType(new IntegerType(1)), "orResult");
                currentFunction.CheckAndSetName(resultAddr.getName(), resultAddr);
                currentFunction.getHeadBB().addInstAtHead(new Store(currentFunction.getHeadBB(), new BoolConstant(false), resultAddr));
//                currentFunction.getHeadBB().addInstAtHead(new BitCastTo(currentFunction.getHeadBB(), resultAddr, orMallocResult, new PointerType(new IntegerType(1))));
                currentFunction.getHeadBB().addInstAtHead(new Call(currentFunction.getHeadBB(), resultAddr, orMalloc, paramForOrMalloc));
//                ArrayList<IROper> paramForOrMalloc = new ArrayList<>();
//                paramForOrMalloc.add(new IntegerConstant(1));
//                IR.Function orMalloc = module.getFunction("malloc");
//                Register orMallocResult = new Register(new PointerType(new IntegerType(8)), "orMallocResult");
//                currentFunction.CheckAndSetName(orMallocResult.getName(), orMallocResult);
//                resultAddr = new Register(new PointerType(new IntegerType(1)), "orResult");
//                currentFunction.CheckAndSetName(resultAddr.getName(), resultAddr);
//                currentFunction.getHeadBB().addInstAtHead(new Store(currentFunction.getHeadBB(), new BoolConstant(false), resultAddr));
//                currentFunction.getHeadBB().addInstAtHead(new BitCastTo(currentFunction.getHeadBB(), resultAddr, orMallocResult, new PointerType(new IntegerType(1))));
//                currentFunction.getHeadBB().addInstAtHead(new Call(currentFunction.getHeadBB(), orMallocResult, orMalloc, paramForOrMalloc));
//                currentFunction.getHeadBB().addInstAtHead(new Alloca(currentFunction.getHeadBB(), resultAddr, new IntegerType(1)));
                opd2BB = new BasicBlock("opd2BB", currentFunction);
                exitBB = new BasicBlock("exitBB", currentFunction);

                opd1Result = node.getOpd1().accept(this);
                opd1BB = currentBB;
                assert opd1Result instanceof ExprResultPair;
//                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
                currentBB.addInstAtTail(new Store(currentBB, ((ExprResultPair) opd1Result).result, resultAddr));
                currentBB.addInstAtTail(new Br(currentBB, ((ExprResultPair) opd1Result).result, exitBB, opd2BB));

                currentBB = opd2BB;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;
                currentBB.addInstAtTail(new Store(currentBB, ((ExprResultPair) opd2Result).result, resultAddr));
                currentBB.addInstAtTail(new Br(currentBB, null, exitBB, null));
                currentFunction.addBasicBlock(opd2BB);
                currentFunction.CheckAndSetName(currentBB.getName(), currentBB);

                currentBB = exitBB;
                result = new Register(new IntegerType(1), "or");
                currentBB.addInstAtTail(new Load(currentBB, result, new IntegerType(1), resultAddr));
                currentFunction.addBasicBlock(currentBB);
                currentFunction.CheckAndSetName(result.getName(), result);
                currentFunction.CheckAndSetName(currentBB.getName(), currentBB);

                return new ExprResultPair(result, null);

//                opd2BB = new BasicBlock("opd2BB", currentFunction);
//                exitBB = new BasicBlock("exitBB", currentFunction);
//
//                opd1Result = node.getOpd1().accept(this);
//                opd1BB = currentBB;
//                assert opd1Result instanceof ExprResultPair;
////                opd1ResultReg = (Register) ((ExprResultPair) opd1Result).result;
//                currentBB.addInstAtTail(new Br(currentBB, ((ExprResultPair) opd1Result).result, exitBB, opd2BB));
//
//                currentBB = opd2BB;
//                opd2Result = node.getOpd2().accept(this);
//                assert opd2Result instanceof ExprResultPair;
////                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;
//                currentBB.addInstAtTail(new Br(currentBB, null, exitBB, null));
//                currentFunction.addBasicBlock(opd2BB);
//                currentFunction.CheckAndSetName(currentBB.getName(), currentBB);
//
//                currentBB = exitBB;
//                result = new Register(new IntegerType(1), "or");
//                possiblePredecessorSet = new LinkedHashSet<>();
//                possiblePredecessorSet.add(new Pair<>(opd1BB, new BoolConstant(true)));
//                possiblePredecessorSet.add(new Pair<>(opd2BB, ((ExprResultPair) opd2Result).result));
//                currentBB.addInstAtTail(new Phi(currentBB, result, possiblePredecessorSet));
//                currentFunction.addBasicBlock(currentBB);
//                currentFunction.CheckAndSetName(result.getName(), result);
//                currentFunction.CheckAndSetName(currentBB.getName(), currentBB);
//
//                return new ExprResultPair(result, null);




            case assign:
                opd1Result = node.getOpd1().accept(this);
                assert opd1Result instanceof ExprResultPair;
                opd2Result = node.getOpd2().accept(this);
                assert opd2Result instanceof ExprResultPair;
                opd1ResultAddr = ((ExprResultPair) opd1Result).addr;
//                if (!(((ExprResultPair) opd2Result).result instanceof Register)) {
//                    assert false;
//                }
//                opd2ResultReg = (Register) ((ExprResultPair) opd2Result).result;

                currentBB.addInstAtTail(new Store(currentBB, ((ExprResultPair) opd2Result).result, opd1ResultAddr));
                return new ExprResultPair(((ExprResultPair) opd2Result).result, null);
        }
        return null;
    }

    @Override
    public Object visit(ThisExpr node) {
        Type2 baseType = node.getScope().getClassType();
        Register thisAddr = (Register) currentFunction.getOperand(baseType.getTypeName() + ".this");
        assert thisAddr.getType() instanceof PointerType;
        IRType type = ((PointerType) thisAddr.getType()).getType();
        Register thisResult = new Register(type, "this");
        currentBB.addInstAtTail(new Load(currentBB, thisResult, type, thisAddr));
        currentFunction.CheckAndSetName(thisResult.getName(), thisResult);
        return new ExprResultPair(thisResult, null);
    }

    @Override
    public Object visit(BoolLiteral node) { //Maybe Done
        return new ExprResultPair(new BoolConstant(node.getVal()), null);
    }

    @Override
    public Object visit(IntLiteral node) { //Maybe Done
        return new ExprResultPair(new IntegerConstant(node.getVal()), null);
    }

    @Override
    public Object visit(StringLiteral node) { //Maybe Done
        StringConstant ini = new StringConstant(node.getVal());
        GlobalVariable stringConst = new GlobalVariable(ini.getType(),
                "str." + String.valueOf(module.getStringConstMapSize()), ini);
        module.addStringConst(stringConst);
        Register result = new Register(new PointerType(new IntegerType(8)), "stringParam");
        ArrayList<IROper> param = new ArrayList<>();
        param.add(new IntegerConstant(0));
        param.add(new IntegerConstant(0));
        GetElementPtr getElementPtr = new GetElementPtr(currentBB, result, stringConst, param);

        currentBB.addInstAtTail(getElementPtr);
        currentFunction.CheckAndSetName(result.getName(), result);
        return new ExprResultPair(result, null);
    }

    @Override
    public Object visit(NullExpr node) { //Maybe Done
        return new ExprResultPair(new NullConstant(), null);
    }

    @Override
    public Object visit(IdentifierExpr node) {
        if (!node.getScope().IsMember(node.getIdentifier())) {
            Entity entity = node.getScope().getEntityForIR(node.getIdentifier());
            IROper addr = entity.getAddr();
            IRType type;
            if (node.getScope().IsGlobalVariable(node.getIdentifier())){
                type = addr.getType();
            } else {
                if (!(addr.getType() instanceof PointerType)) {
                    assert false;
                }
                type = ((PointerType) addr.getType()).getType();
            }
            Register result = new Register(type, node.getIdentifier());
            currentBB.addInstAtTail(new Load(currentBB, result, type, addr));
            currentFunction.CheckAndSetName(node.getIdentifier(), result);
            return new ExprResultPair(result, addr);
        } else {
            if (!(node.getScope().inClassScope()) && (node.getScope().inFunctionScope())) {
                assert false;
            }
            Type2 baseType = node.getScope().getClassType();
            Register thisAddr = (Register) currentFunction.getOperand(baseType.getTypeName() + ".this");
            assert thisAddr.getType() instanceof PointerType;
            IRType type = ((PointerType) thisAddr.getType()).getType();
            Register thisResult = new Register(type, "this");
            currentBB.addInstAtTail(new Load(currentBB, thisResult, type, thisAddr));
            currentFunction.CheckAndSetName(thisResult.getName(), thisResult);

            String identifier = node.getIdentifier();

            ArrayList<VariableEntity> memberList = ((ClassType2) baseType).getMembers();
            AtomicInteger index = new AtomicInteger();
            TypeNode typeNode = null;
            for (VariableEntity member: memberList) {
                if (member.getName().equals(identifier)) {
                    typeNode = member.getType();
                    break;
                }
                index.getAndIncrement();
            }
            assert typeNode != null;
            IRType memberType = module.getIrTypeTable().get(astTypeTable.getType2(typeNode));
            if (memberType instanceof StructureType) {
                memberType = new PointerType(memberType);
            }
            ArrayList<IROper> indexList = new ArrayList<>();
            indexList.add(new IntegerConstant(0));
            indexList.add(new IntegerConstant(index.longValue()));
            Register result = new Register(new PointerType(memberType), baseType.getTypeName() + "." + identifier + "_pointer");
            currentBB.addInstAtTail(new GetElementPtr(currentBB, result, thisResult, indexList));
            Register loadResult = new Register(memberType, baseType.getTypeName() + "." + identifier);
            currentBB.addInstAtTail(new Load(currentBB, loadResult, memberType, result));
            currentFunction.CheckAndSetName(result.getName(), result);
            currentFunction.CheckAndSetName(loadResult.getName(), loadResult);
            return new ExprResultPair(loadResult, result);
        }
    }

    @Override
    public Object visit(VariableList node) { // Done
        return null;
    }
}
