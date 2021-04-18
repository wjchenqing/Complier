package Opt;

import IR.BasicBlock;
import IR.Function;
import IR.Instruction.*;
import IR.Module;
import IR.Operand.*;
import IR.Type.PointerType;
import IR.Type.VoidType;
import Util.Pair;

import java.util.*;

public class SideEffectChecker {
    public enum Scope {
        unknown, local, outer
    }
    private final Module module;

    private final Set<Function> functionsWithSideEffect = new HashSet<>();
    private final Map<Function, Scope> ReturnValueScopes = new HashMap<>();
    private final Map<IROper, Scope> OperandScopes = new HashMap<>();
    private final Queue<Function> queue = new LinkedList<>();
    private final Set<Function> visit = new HashSet<>();

    public SideEffectChecker(Module module) {
        this.module = module;
    }

    public void run() {
        getScopesForOperands();
        checkSideEffect();
    }

    public Set<Function> getFunctionsWithSideEffect() {
        return functionsWithSideEffect;
    }

    public Map<IROper, Scope> getOperandScopes() {
        return OperandScopes;
    }

    private Scope getScope(IROper irOper) {
        if (irOper.getType() instanceof PointerType) {
            return Scope.outer;
        } else {
            return Scope.local;
        }
    }

    public void getScopesForOperands() {
        OperandScopes.clear();
        ReturnValueScopes.clear();
        queue.clear();
        visit.clear();

        for (GlobalVariable globalVariable: module.getGlobalVariableMap().values()) {
            OperandScopes.put(globalVariable, Scope.outer);
        }
        for (Function function: module.getFunctionMap().values()) {
            if (!function.isNotExternal()) {
                ReturnValueScopes.put(function, Scope.local);
                continue;
            }
            for (Parameter parameter: function.getParameters()) {
                OperandScopes.put(parameter, getScope(parameter));
            }
            for (BasicBlock basicBlock: function.getBlockSet()) {
                for (IRInst irInst = basicBlock.getHeadInst(); irInst != null; irInst = irInst.getNextInst()) {
                    Register result = irInst.getResult();
                    if (irInst.getResult() == null) {
                        continue;
                    }
                    Scope scope = getScope(result);
                    if (scope != Scope.local) {
                        scope = Scope.unknown;
                    }
                    OperandScopes.put(result, scope);
                }
            }
            if (function.getFunctionType().getReturnType() instanceof PointerType) {
                ReturnValueScopes.put(function, Scope.outer);
            } else {
                ReturnValueScopes.put(function, Scope.local);
            }
            queue.offer(function);
            visit.add(function);
        }

        while (!queue.isEmpty()) {
            Function function = queue.poll();
            visit.remove(function);

            Queue<BasicBlock> basicBlockQueue = new LinkedList<>();
            Set<BasicBlock> visited = new HashSet<>();

            basicBlockQueue.offer(function.getEntranceBB());
            visited.add(function.getEntranceBB());

            while (!basicBlockQueue.isEmpty()) {
                BasicBlock basicBlock = basicBlockQueue.poll();
                boolean change = false;

                for (IRInst inst = basicBlock.getHeadInst(); inst != null; inst = inst.getNextInst()) {
                    if (inst instanceof Call) {
                        if (OperandScopes.get(inst.getResult()) == Scope.unknown) {
                            OperandScopes.replace(inst.getResult(), ReturnValueScopes.get(((Call) inst).getFunction()));
                            change = true;
                        }
                    } else if (inst instanceof Load) {
                        if (OperandScopes.get(inst.getResult()) == Scope.unknown) {
                            OperandScopes.replace(inst.getResult(), OperandScopes.get(((Load) inst).getPointer()));
                            change = true;
                        }
                    } else if (inst instanceof GetElementPtr) {
                        if (OperandScopes.get(inst.getResult()) == Scope.unknown) {
                            if (((GetElementPtr) inst).getPointer() instanceof NullConstant) {
                                OperandScopes.replace(inst.getResult(), Scope.local);
                            } else {
                                OperandScopes.replace(inst.getResult(), OperandScopes.get(((GetElementPtr) inst).getPointer()));
                            }
                            change = true;
                        }
                    } else if (inst instanceof Phi) {
                        if (OperandScopes.get(inst.getResult()) == Scope.unknown) {
                            boolean isOuter = false;
                            boolean isUnknown = false;
                            for (Pair<BasicBlock, IROper> pair: ((Phi) inst).getPossiblePredecessorSet()) {
                                if (OperandScopes.get(pair.getSecond()) == Scope.outer) {
                                    isOuter = true;
                                } else if (OperandScopes.get(pair.getSecond()) == Scope.unknown) {
                                    isUnknown = true;
                                }
                            }
                            if (isOuter) {
                                OperandScopes.replace(inst.getResult(), Scope.outer);
                                change = true;
                            } else if (isUnknown) {
                                change = false;
                            } else {
                                OperandScopes.replace(inst.getResult(), Scope.local);
                            }
                        }
                    } else if (inst instanceof BitCastTo) {
                        if (OperandScopes.get(inst.getResult()) == Scope.unknown) {
                            if (((BitCastTo) inst).getValue() instanceof NullConstant) {
                                OperandScopes.replace(inst.getResult(), Scope.local);
                            } else {
                                OperandScopes.replace(inst.getResult(), OperandScopes.get(((BitCastTo) inst).getValue()));
                            }
                            change = true;
                        }
                    }
                }

                if (basicBlock.getTailInst() instanceof Br) {
                    Br br = (Br) basicBlock.getTailInst();
                    BasicBlock thenBlock = br.getThenBlock();
                    BasicBlock elseBlock = br.getElseBlock();
                    if (!visited.contains(thenBlock)) {
                        basicBlockQueue.offer(thenBlock);
                        visited.add(thenBlock);
                    } else if (change) {
                        basicBlockQueue.offer(thenBlock);
                    }
                    if (br.getCond() != null) {
                        if (!visited.contains(elseBlock)) {
                            basicBlockQueue.offer(elseBlock);
                            visited.add(elseBlock);
                        } else if (change) {
                            basicBlockQueue.offer(elseBlock);
                        }
                    }
                }
            }

            boolean isLocal = false;
            if (function.getFunctionType().getReturnType() instanceof VoidType) {
                isLocal = true;
            } else if (OperandScopes.get(function.getReturnValue()) == Scope.local) {
                isLocal = true;
            } else {
                continue;
            }

            if (ReturnValueScopes.get(function) != Scope.local) {
                ReturnValueScopes.replace(function, Scope.local);
                for (Call call: function.calls) {
                    Function caller = call.getCurrentBB().getCurrentFunction();
                    if (!visit.contains(caller)) {
                        queue.offer(caller);
                        visit.add(caller);
                    }
                }
            }
        }
    }

    public void checkSideEffect() {
        functionsWithSideEffect.clear();
        queue.clear();

        for (Function function: module.getFunctionMap().values()) {
            if (!function.isNotExternal()) {
                if (function.hasSideEffect) {
                    functionsWithSideEffect.add(function);
                    queue.offer(function);
                }
                continue;
            }
            boolean hasSideEffect = false;
            for (BasicBlock basicBlock: function.getBlockSet()) {
                for (IRInst irInst = basicBlock.getHeadInst(); irInst != null; irInst = irInst.getNextInst()) {
                    if ((irInst instanceof Store) && (OperandScopes.get(((Store) irInst).getPointer()) == Scope.outer)) {
                        hasSideEffect = true;
                        break;
                    }
                }
                if (hasSideEffect) {
                    functionsWithSideEffect.add(function);
                    queue.offer(function);
                    break;
                }
            }
            function.hasSideEffect = hasSideEffect;
        }

        while (!queue.isEmpty()) {
            Function function = queue.poll();
            for (Call call: function.calls) {
                Function caller = call.getCurrentBB().getCurrentFunction();
                if (!caller.hasSideEffect) {
                    functionsWithSideEffect.add(caller);
                    caller.hasSideEffect = true;
                    queue.offer(caller);
                }
            }
        }
    }
}
