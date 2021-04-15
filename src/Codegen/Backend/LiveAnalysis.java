package Codegen.Backend;

import Codegen.BasicBlock;
import Codegen.Function;
import Codegen.Instruction.Instruction;
import Codegen.Module;
import Codegen.Operand.RegisterPhysical;
import Codegen.Operand.RegisterVirtual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class LiveAnalysis {

    static public void analysis(Function function) {
        ArrayList<BasicBlock> dfsList = function.getDfsList();
        for (BasicBlock basicBlock: dfsList) {
            basicBlock.setLiveOut(new LinkedHashSet<>());
            for (Instruction instruction: basicBlock.getInstList()) {
                instruction.addToUEVarVarKill();
            }
        }

//        BasicBlock returnBB = function.getReturnBB();
//        for (int cnt: RegisterPhysical.calleeSaveNum) {
//            RegisterVirtual rv = RegisterPhysical.getVR(cnt);
//            returnBB.addUEVar(rv);
//            returnBB.getVarKill().remove(rv);
//        }
//        RegisterVirtual ra = RegisterPhysical.getVR(1);
//        returnBB.addUEVar(ra);
//        returnBB.getVarKill().remove(ra);
//        if (function.hasReturnVal) {
//            RegisterVirtual a0 = RegisterPhysical.getVR(10);
//            returnBB.addUEVar(a0);
//            returnBB.getVarKill().remove(a0);
//        }

        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = dfsList.size() - 1; i >= 0; --i) {
                BasicBlock basicBlock = dfsList.get(i);
                changed |= setLiveOut(basicBlock);
            }
        }

        BasicBlock returnBB = function.getReturnBB();
        for (int cnt: RegisterPhysical.calleeSaveNum) {
            RegisterVirtual rv = RegisterPhysical.getVR(cnt);
            returnBB.getLiveOut().add(rv);
//            returnBB.addUEVar(rv);
//            returnBB.getVarKill().remove(rv);
        }
        RegisterVirtual ra = RegisterPhysical.getVR(1);
        returnBB.getLiveOut().add(ra);
//        returnBB.addUEVar(ra);
//        returnBB.getVarKill().remove(ra);
//        if (function.hasReturnVal) {
//            RegisterVirtual a0 = RegisterPhysical.getVR(10);
//            returnBB.getLiveOut().add(a0);
//            returnBB.addUEVar(a0);
//            returnBB.getVarKill().remove(a0);
//        }
    }

    static private boolean setLiveOut(BasicBlock basicBlock) {
        Set<RegisterVirtual> liveOut = new LinkedHashSet<>();
        for (BasicBlock successor: basicBlock.getSuccessor()) {
            Set<RegisterVirtual> successorLiveOut = new LinkedHashSet<>(successor.getLiveOut());
            successorLiveOut.removeAll(successor.getVarKill());
            Set<RegisterVirtual> successorUEVar = new LinkedHashSet<>(successor.getUEVar());
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
