package Codegen.Backend;

import Codegen.BasicBlock;
import Codegen.Function;
import Codegen.Instruction.Instruction;
import Codegen.Module;
import Codegen.Operand.RegisterVirtual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class LiveAnalysis {
    private Module module;

    public LiveAnalysis(Module module) {
        this.module = module;
    }

    public void analysis() {
        for (Function function: module.getFunctionMap().values()) {
            if (function.getIrFunction().isNotExternal()) {
                getLiveOut(function);
            }
        }
    }

    private void getLiveOut(Function function) {
        ArrayList<BasicBlock> dfsList = function.getDfsList();
        for (BasicBlock basicBlock: dfsList) {
            for (Instruction instruction: basicBlock.getInstList()) {
                instruction.addToUEVarVarKill();
            }
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = dfsList.size() - 1; i >= 0; --i) {
                BasicBlock basicBlock = dfsList.get(i);
                changed = setLiveOut(basicBlock);
            }
        }
    }

    private boolean setLiveOut(BasicBlock basicBlock) {
        Set<RegisterVirtual> liveOut = new LinkedHashSet<>();
        for (BasicBlock successor: basicBlock.getSuccessor()) {
            Set<RegisterVirtual> successorLiveOut = new HashSet<>(successor.getLiveOut());
            successorLiveOut.removeAll(successor.getVarKill());
            Set<RegisterVirtual> successorUEVar = new HashSet<>(successor.getUEVar());
            successorUEVar.addAll(successorLiveOut);
            liveOut.addAll(successorUEVar);
        }
        if (liveOut.equals(basicBlock.getLiveOut())) {
            return false;
        } else {
            basicBlock.setLiveOut(liveOut);
            return true;
        }
    }
}
