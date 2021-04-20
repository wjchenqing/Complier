package Opt;

import IR.BasicBlock;
import IR.Function;
import IR.Instruction.*;
import IR.Module;
import IR.Operand.*;
import Util.Pair;

import java.util.*;

public class Inline {
    private final Module module;
    private final CFGSimplifier cfgSimplifier;

    private int totalInst;
    private final int totalInstNumLimit = 9000;

    private final Set<Function> recursiveFunctions = new LinkedHashSet<>();
    private final Map<Function, Integer> InstNumMap = new LinkedHashMap<>();
    private final Map<Function, Set<Function>> calleeSetMap = new LinkedHashMap<>();
    private final Map<Function, Set<Call>> callSetMap = new LinkedHashMap<>();  //All Call inst in the function.
                                                                                //All call inst that call this func are in function.calls.

    private BasicBlock copiedEntrance = null;
    private BasicBlock copiedReturnBB = null;
    private final Map<BasicBlock, BasicBlock> oldAndNewBBs = new HashMap<>();
    private final Map<IROper, IROper> oldAndNewOperands = new HashMap<>();
    private final Set<Function> visited = new HashSet<>();

    private boolean changed = false;

    public Inline(Module module, CFGSimplifier cfgSimplifier) {
        this.module = module;
        this.cfgSimplifier = cfgSimplifier;
    }

    public boolean run() {
        changed = false;
        recursiveFunctions.clear();
        InstNumMap.clear();
        callSetMap.clear();
        calleeSetMap.clear();
        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal() && !function.getBlockSet().isEmpty()) {
                callSetMap.put(function, new LinkedHashSet<>());
                calleeSetMap.put(function, new LinkedHashSet<>());
            }
        }

        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal() && !function.getBlockSet().isEmpty()) {
                int num = countInstNum(function);
                InstNumMap.put(function, num);
                totalInst += num;
            }
        }

        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal() && !function.getBlockSet().isEmpty()) {
                visited.clear();
                if (willCallFunction(function, function)) {
                    recursiveFunctions.add(function);
                }
            }
        }

        changed = NonRecursiveFuncInline();
        return changed;
    }

    private boolean NonRecursiveFuncInline() {
        boolean changed = false;
        while (true) {
            Function source = null;
            int calleeNum = 100000;
            for (Function function: calleeSetMap.keySet()) {
                if (!recursiveFunctions.contains(function)) {
                    int size = callSetMap.get(function).size();
                    if (source == null) {
                        source = function;
                        calleeNum = size;
                    } else if (size < calleeNum) {
                        source = function;
                        calleeNum = size;
                    }
                }
            }
            if (source == null) {
                break;
            }
            changed = true;
            Set<Call> calls = new LinkedHashSet<>(source.calls);
            for (Call callerLocation: calls) {
                if (!source.calls.contains(callerLocation)) {
                    continue;
                }
                BasicBlock callerBlock = callerLocation.getCurrentBB();
                Function caller = callerBlock.getCurrentFunction();
                BasicBlock splitResult = callerBlock.split(callerLocation);
                copyFunction(caller, source, callerBlock.depth, callerLocation);
                callerBlock.getTailInst().deleteInst();
                assert copiedEntrance != null;
                callerBlock.addInstAtTail(new Br(callerBlock, null, copiedEntrance, null));
                copiedReturnBB.addInstAtTail(new Br(copiedReturnBB, null, splitResult, null));
                callSetMap.get(caller).remove(callerLocation);
                callerLocation.deleteInst();
                totalInst += InstNumMap.get(source);
                for (IROper irOper: oldAndNewOperands.values()) {
                    if (irOper.isConstant()) {
                        Set<IRInst> use = new LinkedHashSet<>(irOper.getUses());
                        for (IRInst irInst : use) {
                            irInst.replaceUse(irOper, irOper);
                        }
                    }
                }
                cfgSimplifier.deleteBBWithoutPredecessor(caller);
                Set<BasicBlock> blockSet = new LinkedHashSet<>(caller.getBlockSet());
                boolean flag = true;
                while (flag) {
                    flag = false;
                    for (BasicBlock basicBlock : blockSet) {
                        if (!caller.getBlockSet().contains(basicBlock)) {
                            continue;
                        }
                        for (IRInst phi = basicBlock.getHeadInst(); phi instanceof Phi; phi = phi.getNextInst()) {
                            flag |= ((Phi) phi).Check();
                        }
                    }
                }
            }
            if (source.calls.isEmpty() && !calls.isEmpty()) {
                module.getFunctionMap().remove(source.getName());
                totalInst -= InstNumMap.get(source);
            }
            calleeSetMap.remove(source);
        }
        return changed;
    }

    private int countInstNum(Function function) {
        int instNum = 0;
        Set<Call> callSet = callSetMap.get(function);
        Set<Function> calleeSet = calleeSetMap.get(function);
        for (BasicBlock basicBlock: function.getBlockSet()) {
            for (IRInst irInst = basicBlock.getHeadInst(); irInst != null; irInst = irInst.getNextInst()) {
                instNum++;
                if (irInst instanceof Call) {
                    if (((Call) irInst).getFunction().isNotExternal()) {
                        callSet.add((Call) irInst);
                        calleeSet.add(((Call) irInst).getFunction());
                    }
                }
            }
        }
        return instNum;
    }

    private boolean willCallFunction(Function cur, Function target) {
        visited.add(cur);
        if (calleeSetMap.containsKey(cur)) {
            Set<Function> calleeSet = calleeSetMap.get(cur);
            if (calleeSet.contains(target)) {
                return true;
            }
            for (Function function: calleeSet) {
                if ((!visited.contains(function)) && willCallFunction(function, target)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void copyFunction(Function targetFunc, Function sourceFunc, int depth, Call call) {
        copiedEntrance = null;
        copiedReturnBB = null;
        oldAndNewBBs.clear();
        oldAndNewOperands.clear();
        for (int i = 0; i < sourceFunc.getParameters().size(); ++i) {
            oldAndNewOperands.put(sourceFunc.getParameters().get(i), call.getParams().get(i));
        }
        for (BasicBlock basicBlock: sourceFunc.getBlockSet()) {
            BasicBlock newBB = new BasicBlock(basicBlock.getName() + "_copy", targetFunc, depth + basicBlock.depth);
            targetFunc.CheckAndSetName(newBB.getName(), newBB);
            targetFunc.addBasicBlock(newBB);
            targetFunc.computePostReverseDFSListAgain = true;
            targetFunc.computePostDFSListAgain = true;
            targetFunc.computeDFSListAgain = true;
            oldAndNewBBs.put(basicBlock, newBB);
        }
        for (BasicBlock basicBlock: sourceFunc.getBlockSet()) {
            copyBlock(targetFunc, basicBlock, call);
        }
        assert !oldAndNewBBs.isEmpty();
        copiedEntrance = oldAndNewBBs.get(sourceFunc.getEntranceBB());
        copiedReturnBB = oldAndNewBBs.get(sourceFunc.getReturnBB());
    }

    private IROper getNewIROperand(Function targetFunc, IROper sourceIROperand) {
        if (oldAndNewOperands.containsKey(sourceIROperand)) {
            return oldAndNewOperands.get(sourceIROperand);
        } else {
            if (sourceIROperand instanceof BoolConstant) {
                BoolConstant newIROperand = new BoolConstant(((BoolConstant) sourceIROperand).getValue());
                oldAndNewOperands.put(sourceIROperand, newIROperand);
                return newIROperand;
            } else if (sourceIROperand instanceof IntegerConstant) {
                IntegerConstant newIROperand = new IntegerConstant(((IntegerConstant) sourceIROperand).getValue());
                oldAndNewOperands.put(sourceIROperand, newIROperand);
                return newIROperand;
            } else if (sourceIROperand instanceof GlobalVariable) {
                oldAndNewOperands.put(sourceIROperand, sourceIROperand);
                return sourceIROperand;
            } else if (sourceIROperand instanceof NullConstant) {
                NullConstant newIROperand = new NullConstant();
                oldAndNewOperands.put(sourceIROperand, newIROperand);
                return newIROperand;
            } else if (sourceIROperand instanceof Register) {
                Register newIROperand = new Register(sourceIROperand.getType(), sourceIROperand.getName() + "_copy");
                targetFunc.CheckAndSetName(newIROperand.getName(), newIROperand);
                oldAndNewOperands.put(sourceIROperand, newIROperand);
                return newIROperand;
            } else {
                assert false;
                return null;
            }
        }
    }

    private void copyBlock(Function targetFunc, BasicBlock sourceBlock, Call call) {
        BasicBlock targetBlock = oldAndNewBBs.get(sourceBlock);
        for (IRInst sourceInst = sourceBlock.getHeadInst(); sourceInst != null; sourceInst = sourceInst.getNextInst()) {
            if (sourceInst instanceof BinaryOperation) {
                Register newResult = (Register) getNewIROperand(targetFunc, sourceInst.getResult());
                IROper newOpt1 = getNewIROperand(targetFunc, ((BinaryOperation) sourceInst).getOp1());
                IROper newOpt2 = getNewIROperand(targetFunc, ((BinaryOperation) sourceInst).getOp2());
                assert newOpt1 != null;
                targetBlock.addInstAtTail(new BinaryOperation(targetBlock, newResult, ((BinaryOperation) sourceInst).getOp(),
                        ((BinaryOperation) sourceInst).getType(), newOpt1, newOpt2));
            } else if (sourceInst instanceof BitCastTo) {
                Register newResult = (Register) getNewIROperand(targetFunc, sourceInst.getResult());
                IROper newValue = getNewIROperand(targetFunc, ((BitCastTo) sourceInst).getValue());
                assert newValue != null;
                targetBlock.addInstAtTail(new BitCastTo(targetBlock, newResult, newValue, ((BitCastTo) sourceInst).getTargetType()));
            } else if (sourceInst instanceof Br) {
                if (((Br) sourceInst).getCond() != null) {
                    IROper newCond = getNewIROperand(targetFunc, ((Br) sourceInst).getCond());
                    BasicBlock newThenBlock = oldAndNewBBs.get(((Br) sourceInst).getThenBlock());
                    BasicBlock newElseBlock = oldAndNewBBs.get(((Br) sourceInst).getElseBlock());
                    targetBlock.addInstAtTail(new Br(targetBlock, newCond, newThenBlock, newElseBlock));
                } else {
                    BasicBlock newThenBlock = oldAndNewBBs.get(((Br) sourceInst).getThenBlock());
                    targetBlock.addInstAtTail(new Br(targetBlock, null, newThenBlock, null));
                }
            } else if (sourceInst instanceof Call) {
                Register newResult = sourceInst.getResult() != null ? (Register) getNewIROperand(targetFunc, sourceInst.getResult()) : null;
                ArrayList<IROper> newParams = new ArrayList<>();
                for (IROper param: ((Call) sourceInst).getParams()) {
                    newParams.add(getNewIROperand(targetFunc, param));
                }
                targetBlock.addInstAtTail(new Call(targetBlock, newResult, ((Call) sourceInst).getFunction(), newParams));
            } else if (sourceInst instanceof GetElementPtr) {
                Register newResult = (Register) getNewIROperand(targetFunc, sourceInst.getResult());
                IROper newPointer = getNewIROperand(targetFunc, ((GetElementPtr) sourceInst).getPointer());
                ArrayList<IROper> newIndexes = new ArrayList<>();
                for (IROper index: ((GetElementPtr) sourceInst).getIdxes()) {
                    newIndexes.add(getNewIROperand(targetFunc, index));
                }
                assert newPointer != null;
                targetBlock.addInstAtTail(new GetElementPtr(targetBlock, newResult, newPointer, newIndexes));
            } else if (sourceInst instanceof Icmp) {
                Register newResult = (Register) getNewIROperand(targetFunc, sourceInst.getResult());
                IROper newOpt1 = getNewIROperand(targetFunc, ((Icmp) sourceInst).getOp1());
                IROper newOpt2 = getNewIROperand(targetFunc, ((Icmp) sourceInst).getOp2());
                assert newResult != null;
                assert newOpt1 != null;
                assert newOpt2 != null;
                targetBlock.addInstAtTail(new Icmp(targetBlock, newResult, ((Icmp) sourceInst).getCond(),
                        ((Icmp) sourceInst).getType(), newOpt1, newOpt2));
            } else if (sourceInst instanceof Load) {
                Register newResult = (Register) getNewIROperand(targetFunc, sourceInst.getResult());
                IROper newPointer = getNewIROperand(targetFunc, ((Load) sourceInst).getPointer());
                targetBlock.addInstAtTail(new Load(targetBlock, newResult, ((Load) sourceInst).getType(), newPointer));
            } else if (sourceInst instanceof Phi) {
                Register newResult = (Register) getNewIROperand(targetFunc, sourceInst.getResult());
                Set<Pair<BasicBlock, IROper>> newSet = new LinkedHashSet<>();
                for (Pair<BasicBlock, IROper> pair: ((Phi) sourceInst).getPossiblePredecessorSet()) {
                    BasicBlock newBB = oldAndNewBBs.get(pair.getFirst());
                    IROper newIROperand = getNewIROperand(targetFunc, pair.getSecond());
                    newSet.add(new Pair<>(newBB, newIROperand));
                }
                targetBlock.addInstAtTail(new Phi(targetBlock, newResult, newSet));
            } else if (sourceInst instanceof Ret) {
                if (((Ret) sourceInst).getReturnVal() != null){
                    IROper o = call.getResult();
                    IROper n = getNewIROperand(targetFunc, ((Ret) sourceInst).getReturnVal());
                    assert n != null;
                    n.getUses().addAll(o.getUses());
                    for (IRInst use : call.getResult().getUses()) {
                        use.replaceUse(o, n);
                    }
                }
            } else if (sourceInst instanceof Store) {
                IROper newValue = getNewIROperand(targetFunc, ((Store) sourceInst).getValue());
                IROper newPointer = getNewIROperand(targetFunc, ((Store) sourceInst).getPointer());
                targetBlock.addInstAtTail(new Store(targetBlock, newValue, newPointer));
            }
        }
    }
}