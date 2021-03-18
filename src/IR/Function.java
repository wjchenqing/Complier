package IR;

import IR.Instruction.Alloca;
import IR.Instruction.Load;
import IR.Instruction.Ret;
import IR.Instruction.Store;
import IR.Operand.IROper;
import IR.Operand.Parameter;
import IR.Operand.Register;
import IR.Type.FunctionType;
import IR.Type.IRType;
import IR.Type.PointerType;
import IR.Type.VoidType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Function {
    private String name;
    private IRType returnType;
    private FunctionType functionType;
    private ArrayList<Parameter> parameters;
    private BasicBlock headBB = null;
    private BasicBlock tailBB = null;
    private BasicBlock returnBB = null;
    private Register returnValue = null;
    private final Map<String, Object> OperandMap = new HashMap<>();

    private boolean notExternal;

    public Function(String name,IRType returnType, FunctionType functionType, ArrayList<Parameter> parameters, boolean notExternalThusShouldInitial) {
        this.name = name;
        this.returnType = returnType;
        this.functionType = functionType;
        this.parameters = parameters;
        this.notExternal = notExternalThusShouldInitial;

        if (notExternalThusShouldInitial) {
            BasicBlock basicBlock = new BasicBlock(name + ".headBB", this);
            addBasicBlock(basicBlock);
            OperandMap.put(headBB.getName(), basicBlock);

            returnBB = new BasicBlock(name + ".returnBB", this);
            OperandMap.put(returnBB.getName(), returnBB);
            if ((returnType == null) || (returnType instanceof VoidType)) {
                returnBB.addInstAtTail(new Ret(returnBB, new VoidType(), null));
            } else {
                returnValue = new Register(new PointerType(returnType), name + ".returnValue");
                headBB.addInstAtTail(new Alloca(headBB, returnValue, returnType));
                headBB.addInstAtTail(new Store(headBB, returnType.defaultOperand(), returnValue));
                OperandMap.put(returnValue.getName(), returnValue);
                Register returnValueRegister = new Register(returnType, name + "returnValueRegister");
                returnBB.addInstAtTail(new Load(returnBB, returnValueRegister, returnType, returnValue));
                returnBB.addInstAtTail(new Ret(returnBB, returnType, returnValueRegister));
                OperandMap.put(returnValueRegister.getName(), returnValueRegister);
            }
        }
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
        if (headBB == null) {
            headBB = basicBlock;
        } else {
            tailBB.setNextBB(basicBlock);
            basicBlock.setPrevBB(tailBB);
        }
        tailBB = basicBlock;
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

    public BasicBlock getHeadBB() {
        return headBB;
    }

    public BasicBlock getTailBB() {
        return tailBB;
    }

    public BasicBlock getReturnBB() {
        return returnBB;
    }

    public Register getReturnValue() {
        return returnValue;
    }

    public void setHeadBB(BasicBlock headBB) {
        this.headBB = headBB;
    }

    public void setTailBB(BasicBlock tailBB) {
        this.tailBB = tailBB;
    }

    public void setReturnBB(BasicBlock returnBB) {
        this.returnBB = returnBB;
    }

    public void setReturnValue(Register returnValue) {
        this.returnValue = returnValue;
    }

    public ArrayList<BasicBlock> getBlockList() {
        ArrayList<BasicBlock> basicBlocks = new ArrayList<>();
        for (BasicBlock cur = headBB; cur != null; cur = cur.getNextBB()) {
            basicBlocks.add(cur);
        }
        return basicBlocks;
    }

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
            string.append(paramType.toString()).append(" %").append(parameters.get(i.get()-1).getParamName());
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
}
