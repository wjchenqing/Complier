package IR;

import IR.Instruction.*;
import IR.Operand.IROper;
import IR.Operand.Parameter;
import IR.Operand.Register;
import IR.Type.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Function {
    private String name;
    private IRType returnType;
    private FunctionType functionType;
    private ArrayList<Parameter> parameters;
    private BasicBlock entranceBB = null;
    private Set<BasicBlock> blockSet = new LinkedHashSet<>();
    private BasicBlock returnBB = null;
    private Register returnValue = null;
    private final Map<String, Object> OperandMap = new HashMap<>();
    public final Map<String, Register> registerMap = new HashMap<>();
    public final Set<Register> defs = new LinkedHashSet<>();

    public Set<Register> allocaResults = new LinkedHashSet<>();

    public Set<Call> calls = new LinkedHashSet<>();
    public Set<Call> callInstSet = new LinkedHashSet<>();
    public boolean callItSelf = false;
    public Set<Function> getCallers() {
        Set<Function> ans = new HashSet<>();
        for (Call call: calls) {
            ans.add(call.getFunction());
        }
        return ans;
    }

    public boolean hasSideEffect;

    private final boolean notExternal;

    private final ArrayList<BasicBlock> dfsList = new ArrayList<>();
    private final ArrayList<BasicBlock> postDfsList = new ArrayList<>();
    private final ArrayList<BasicBlock> postReverseDFSList = new ArrayList<>();
    private Set<BasicBlock> visited = new HashSet<>();


    public Function(Module module, String name,IRType returnType, FunctionType functionType, ArrayList<Parameter> parameters, boolean notExternalThusShouldInitial, boolean hasSideEffect) {
        this.name = name;
        this.returnType = returnType;
        this.functionType = functionType;
        this.parameters = parameters;
        this.notExternal = notExternalThusShouldInitial;
        this.hasSideEffect = hasSideEffect;

        if (notExternalThusShouldInitial) {
            BasicBlock basicBlock = new BasicBlock(name + ".headBB", this, 0);
            addBasicBlock(basicBlock);
            OperandMap.put(entranceBB.getName(), basicBlock);

            returnBB = new BasicBlock(name + ".returnBB", this, 0);
            OperandMap.put(returnBB.getName(), returnBB);
            blockSet.add(returnBB);
            if ((returnType == null) || (returnType instanceof VoidType)) {
                returnBB.addInstAtTail(new Ret(returnBB, new VoidType(), null));
            } else {
//                ArrayList<IROper> paramForMalloc = new ArrayList<>();
//                paramForMalloc.add(new IntegerConstant(new PointerType(returnType).getByte()));
//                Function mallocFunc = module.getFunction("malloc");
//                Register mallocAddr = new Register(new PointerType(new IntegerType(8)), "mallocAddr");
//                CheckAndSetName(mallocAddr.getName(), mallocAddr);
                returnValue = new Register(new PointerType(returnType), name + ".returnValue");
//                headBB.addInstAtHead(new Store(headBB, returnType.defaultOperand(), returnValue));
//                headBB.addInstAtHead(new BitCastTo(headBB, returnValue, mallocAddr, new PointerType(returnType)));
//                headBB.addInstAtHead(new Call(headBB, returnValue, mallocFunc, paramForMalloc));
//                ArrayList<IROper> paramForMalloc = new ArrayList<>();
//                paramForMalloc.add(new IntegerConstant(new PointerType(returnType).getByte()));
//                Function mallocFunc = module.getFunction("malloc");
//                Register mallocAddr = new Register(new PointerType(new IntegerType(8)), "mallocAddr");
//                CheckAndSetName(mallocAddr.getName(), mallocAddr);
//                returnValue = new Register(new PointerType(returnType), name + ".returnValue");
//                headBB.addInstAtHead(new Store(headBB, returnType.defaultOperand(), returnValue));
//                headBB.addInstAtHead(new BitCastTo(headBB, returnValue, mallocAddr, new PointerType(returnType)));
//                headBB.addInstAtHead(new Call(headBB, mallocAddr, mallocFunc, paramForMalloc));
                entranceBB.addInstAtTail(new Alloca(entranceBB, returnValue, returnType));
                entranceBB.addInstAtTail(new Store(entranceBB, returnType.defaultOperand(), returnValue));
                OperandMap.put(returnValue.getName(), returnValue);
                Register returnValueRegister = new Register(returnType, name + "returnValueRegister");
                returnBB.addInstAtTail(new Load(returnBB, returnValueRegister, returnType, returnValue));
                returnBB.addInstAtTail(new Ret(returnBB, returnType, returnValueRegister));
                OperandMap.put(returnValueRegister.getName(), returnValueRegister);
            }
        }
    }

    public Set<BasicBlock> getBlockSet() {
        return blockSet;
    }

    public boolean isNotExternal() {
        return notExternal;
    }

    public void setFunctionType(FunctionType functionType) {
        this.functionType = functionType;
    }

    public void setParameters(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
    }

    public void CheckParameterType() {
        if (!(parameters.size() == functionType.getParamTypeList().size() &&
                IntStream.range(0, parameters.size()).allMatch(i ->
                        (parameters.get(i).getType().equals(functionType.getParamTypeList().get(i)))))) {
            assert false;
        }
    }

    public void addBasicBlock(BasicBlock basicBlock) {
        if (entranceBB == null) {
            entranceBB = basicBlock;
        }
        blockSet.add(basicBlock);
    }

    public Object getOperand(String name) {
        return OperandMap.get(name);
    }

    public void CheckAndSetName(String name, IROper operand) {
        int tag = 0;
        String tmp = name;
        while (OperandMap.containsKey(tmp)) {
            tmp = name + "_" + tag;
            tag++;
        }
        OperandMap.put(tmp, operand);
        operand.setName(tmp);
        if (operand instanceof Register) {
            registerMap.put(tmp, (Register) operand);
        }
    }

    public void CheckAndSetName(String name, BasicBlock operand) {
        int tag = 0;
        String tmp = name;
        while (OperandMap.containsKey(tmp)) {
            tmp = name + "_" + tag;
            tag++;
        }
        OperandMap.put(tmp, operand);
        operand.setName(tmp);
    }

    public String getName() {
        return name;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public BasicBlock getEntranceBB() {
        if (blockSet.contains(entranceBB)) {
            return entranceBB;
        } else {
            for (BasicBlock basicBlock: blockSet) {
                if (basicBlock.getPredecessor().isEmpty()) {
                    entranceBB = basicBlock;
                    return basicBlock;
                }
            }
            assert false;
            return null;
        }
    }

//    public BasicBlock getTailBB() {
//        return tailBB;
//    }

    public BasicBlock getReturnBB() {
        return returnBB;
    }

    public Register getReturnValue() {
        return returnValue;
    }

    public void setEntranceBB(BasicBlock entranceBB) {
        this.entranceBB = entranceBB;
    }

//    public void setTailBB(BasicBlock tailBB) {
//        this.tailBB = tailBB;
//    }

    public void setReturnBB(BasicBlock returnBB) {
        this.returnBB = returnBB;
    }

    public void setReturnValue(Register returnValue) {
        this.returnValue = returnValue;
    }

//    public ArrayList<BasicBlock> getBlockList() {
//        ArrayList<BasicBlock> basicBlocks = new ArrayList<>();
//        for (BasicBlock cur = entranceBB; cur != null; cur = cur.getNextBB()) {
//            basicBlocks.add(cur);
//        }
//        return basicBlocks;
//    }

    @Override
    public String toString() {

        return "@" + name;
    }

    public String printer() {
        StringBuilder string = new StringBuilder();
        string.append(returnType.toString()).append(" @").append(name).append("(");
        int bound = functionType.getParamTypeList().size();
        AtomicInteger i = new AtomicInteger(1);
        for (IRType paramType: functionType.getParamTypeList()) {
            string.append(paramType.toString()).append(" %").append(parameters.get(i.get()-1).getName());
            if (i.get() != bound) {
                string.append(", ");
            }
            i.incrementAndGet();
        }
        string.append(")");
        return string.toString();
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }

    public boolean computeDFSListAgain = true;

    public ArrayList<BasicBlock> getDfsList() {
        visited.clear();
        if (computeDFSListAgain) {
            dfsList.clear();
            dfs(entranceBB, 1);
            computeDFSListAgain = false;
        }
        return dfsList;
    }

    private void dfs(BasicBlock basicBlock, int n) {
        int cnt = n;
        dfsList.add(basicBlock);
        visited.add(basicBlock);
        basicBlock.dfsNum = cnt;
        ++cnt;
        for (BasicBlock bb: basicBlock.getSuccessor()) {
            if (!visited.contains(bb)) {
                dfs(bb, cnt);
                ++cnt;
            }
        }
    }

    public boolean computePostDFSListAgain = true;

    public ArrayList<BasicBlock> getPostDfsList() {
        visited = new HashSet<>();
        if (computePostDFSListAgain) {
            postDfsList.clear();
            post(entranceBB);
            computePostDFSListAgain = false;
        }
        return postDfsList;
    }

    private void post(BasicBlock basicBlock) {
        visited.add(basicBlock);
        for (BasicBlock bb: basicBlock.getSuccessor()) {
            if (!visited.contains(bb)) {
                post(bb);
            }
        }
        postDfsList.add(basicBlock);
        basicBlock.postDfsNum = postDfsList.size();
    }

    public boolean computePostReverseDFSListAgain = true;

    public ArrayList<BasicBlock> getPostReverseDFSList() {
        visited = new HashSet<>();
        if (computePostReverseDFSListAgain) {
            postReverseDFSList.clear();
            postReverse(returnBB);
            computePostReverseDFSListAgain = false;
        }
        return postReverseDFSList;
    }

    private void postReverse(BasicBlock basicBlock) {
        visited.add(basicBlock);
        for (BasicBlock bb: basicBlock.getPredecessor()) {
            if (!visited.contains(bb)) {
                postReverse(bb);
            }
        }
        postReverseDFSList.add(basicBlock);
        basicBlock.postReverseDFSNum = postReverseDFSList.size();
    }
}
