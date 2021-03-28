package Codegen.Backend;

import Codegen.Instruction.*;
import Codegen.Operand.*;
import Codegen.Stack;
import IR.BasicBlock;
import IR.Function;
import IR.IRVisitor;
import IR.Instruction.*;
import IR.Instruction.Call;
import IR.Instruction.Store;
import IR.Module;
import IR.Operand.*;
import IR.Operand.Register;
import IR.Type.*;

import java.util.ArrayList;

public class InstructionSelector implements IRVisitor {
    private final Codegen.Module asmModule = new Codegen.Module();
    private Codegen.Function curFunction = null;
    private Codegen.BasicBlock curBlock = null;

    public Codegen.Module getModule() {
        return asmModule;
    }

    public Codegen.Function getCurFunction() {
        return curFunction;
    }

    public Codegen.BasicBlock getCurBlock() {
        return curBlock;
    }

    public void setCurFunction(Codegen.Function curFunction) {
        this.curFunction = curFunction;
    }

    public void setCurBlock(Codegen.BasicBlock curBlock) {
        this.curBlock = curBlock;
    }

    @Override
    public void visit(Module module) {
        for (GlobalVariable globalVariable: module.getGlobalVariableMap().values()) {
            GlobalVar globalVar = new GlobalVar(globalVariable.getName());
            asmModule.addGlobalVar(globalVar);

            IRType irType = globalVariable.getType();
            IROper initial = globalVariable.getValue();

            if (irType.equals(new IntegerType(1))) {
                assert initial instanceof BoolConstant;
                globalVar.setBoolByte(((BoolConstant) initial).getValue() ? 1 : 0);
            } else if (irType.equals(new IntegerType(32))) {
                assert initial instanceof IntegerConstant;
                globalVar.setIntWord((int) ((IntegerConstant) initial).getValue());
            } else if (irType instanceof PointerType) {
                assert initial instanceof NullConstant;
                globalVar.setIntWord(0);
            } else {
                assert false;
            }
        }

        for (GlobalVariable globalVariable: module.getStringConstMap().values()) {
            GlobalVar globalVar = new GlobalVar(globalVariable.getName());
            asmModule.addGlobalVar(globalVar);

            assert globalVariable.getType() instanceof ArrayType;
            assert globalVariable.getValue() instanceof StringConstant;

            globalVar.setStringAsciz(((StringConstant) globalVariable.getValue()).getValue());

        }

        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal()) {
                asmModule.addFunction(new Codegen.Function(function.getName(), asmModule, function));
            } else {
                asmModule.addFunction(new Codegen.Function(function.getName(), asmModule, function));
            }
        }

        for (Function function: module.getFunctionMap().values()) {
            if (function.isNotExternal()) {
                function.accept(this);
            }
        }
    }

    @Override
    public void visit(Function function) {
        String functionName = function.getName();
        curFunction = getModule().getFunction(functionName);
        Stack stack = new Stack(curFunction);
        curFunction.setStack(stack);
        curBlock = curFunction.getHeadBB();

        RegisterVirtual ra = new RegisterVirtual("ra.save");
        curFunction.CheckAndSetName(ra.getName(), ra);
        curBlock.addInst(new Move(curBlock, ra, RegisterPhysical.getVR(1)));

        for (int num: RegisterPhysical.calleeSaveNum) {
            RegisterVirtual rv = new RegisterVirtual("x" + num);
            curFunction.CheckAndSetName(rv.getName(), rv);
            curBlock.addInst(new Move(curBlock, rv, RegisterPhysical.getVR(num)));
        }

        int minNum = Integer.min(function.getParameters().size(), 8);
        for (int i = 0; i < minNum; ++i) {
            RegisterVirtual rv = curFunction.getRV(function.getParameters().get(i).getName());
            curBlock.addInst(new Move(curBlock, rv, RegisterPhysical.getVR(10 + i)));
        }

        for (int i = 8; i < function.getParameters().size(); ++i) {
            RegisterVirtual rv = curFunction.getRV(function.getParameters().get(i).getName());
            Addr addr = new Addr(true, new RegisterVirtual(rv.getName() + "_addr"), null);
            stack.addFormalParamAddr(addr);
            curBlock.addInst(new LoadGlobal(curBlock, LoadGlobal.Name.lw, rv, addr));
        }

        for (BasicBlock basicBlock: function.getBlockList()) {
            basicBlock.accept(this);
        }
        function.getReturnBB().accept(this);
    }

    @Override
    public void visit(BasicBlock basicBlock) {
        curBlock = curFunction.getBasicBlock(basicBlock.getName());
        for (IRInst irInst: basicBlock.getInstList()) {
            irInst.accept(this);
//            curBlock.simplifyMove();
        }
    }

    @Override
    public void visit(Alloca alloca) {
        // Done.
    }

    @Override
    public void visit(BinaryOperation binary) {
        RegisterVirtual result = curFunction.getRV(binary.getResult().getName());
        IROper irOp1 = binary.getOp1();
        IROper irOp2 = binary.getOp2();
        Operand op1;
        Operand op2;
        BinaryInstruction.Name name;
        BinaryOperation.BinaryOp op = binary.getOp();
        switch (op) {
            case add:
                op2 = getOperand(irOp2);
                if (op2 instanceof Immediate) {
                    op1 = getReg(irOp1);
                    name = BinaryInstruction.Name.addi;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, result, (Codegen.Operand.Register) op1, op2));
                } else {
                    assert op2 instanceof RegisterVirtual;
                    op1 = getOperand(irOp1);
                    if (op1 instanceof Immediate) {
                        name = BinaryInstruction.Name.addi;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, true, result, (Codegen.Operand.Register) op2, op1));
                    } else {
                        name = BinaryInstruction.Name.add;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, false, result, (Codegen.Operand.Register) op1, op2));
                    }
                }
                return;
            case or:
                op2 = getOperand(irOp2);
                if (op2 instanceof Immediate) {
                    op1 = getReg(irOp1);
                    name = BinaryInstruction.Name.ori;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, result, (Codegen.Operand.Register) op1, op2));
                } else {
                    assert op2 instanceof RegisterVirtual;
                    op1 = getOperand(irOp1);
                    if (op1 instanceof Immediate) {
                        name = BinaryInstruction.Name.ori;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, true, result, (Codegen.Operand.Register) op2, op1));
                    } else {
                        name = BinaryInstruction.Name.or;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, false, result, (Codegen.Operand.Register) op1, op2));
                    }
                }
                return;
            case xor:
                op2 = getOperand(irOp2);
                if (op2 instanceof Immediate) {
                    op1 = getReg(irOp1);
                    name = BinaryInstruction.Name.xori;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, result, (Codegen.Operand.Register) op1, op2));
                } else {
                    assert op2 instanceof RegisterVirtual;
                    op1 = getOperand(irOp1);
                    if (op1 instanceof Immediate) {
                        name = BinaryInstruction.Name.xori;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, true, result, (Codegen.Operand.Register) op2, op1));
                    } else {
                        name = BinaryInstruction.Name.xor;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, false, result, (Codegen.Operand.Register) op1, op2));
                    }
                }
                return;
            case and:
                op2 = getOperand(irOp2);
                if (op2 instanceof Immediate) {
                    op1 = getReg(irOp1);
                    name = BinaryInstruction.Name.andi;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, result, (Codegen.Operand.Register) op1, op2));
                } else {
                    assert op2 instanceof RegisterVirtual;
                    op1 = getOperand(irOp1);
                    if (op1 instanceof Immediate) {
                        name = BinaryInstruction.Name.andi;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, true, result, (Codegen.Operand.Register) op2, op1));
                    } else {
                        name = BinaryInstruction.Name.and;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, false, result, (Codegen.Operand.Register) op1, op2));
                    }
                }
                return;
            case sub:
                op2 = getOperand(irOp2);
                op1 = getReg(irOp1);
                if (op2 instanceof ImmediateInt) {
                    name = BinaryInstruction.Name.addi;
                    ((ImmediateInt) op2).negation();
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, result, (Codegen.Operand.Register) op1, op2));
                } else {
                    name = BinaryInstruction.Name.sub;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, false, result, (Codegen.Operand.Register) op1, op2));
                }
                return;
            case mul:
                op1 = getReg(irOp1);
                op2 = getReg(irOp2);
                name = BinaryInstruction.Name.mul;
                curBlock.addInst(new BinaryInstruction(curBlock, name, false, result, (Codegen.Operand.Register) op1, op2));
                return;
            case sdiv:
                op1 = getReg(irOp1);
                op2 = getReg(irOp2);
                name = BinaryInstruction.Name.div;
                curBlock.addInst(new BinaryInstruction(curBlock, name, false, result, (Codegen.Operand.Register) op1, op2));
                return;
            case srem:
                op1 = getReg(irOp1);
                op2 = getReg(irOp2);
                name = BinaryInstruction.Name.rem;
                curBlock.addInst(new BinaryInstruction(curBlock, name, false, result, (Codegen.Operand.Register) op1, op2));
                return;
            case shl:
                op1 = getReg(irOp1);
                op2 = getOperand(irOp2);
                if (op2 instanceof ImmediateInt) {
                    name = BinaryInstruction.Name.slli;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, result, (Codegen.Operand.Register) op1, op2));
                } else {
                    name = BinaryInstruction.Name.sll;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, false, result, (Codegen.Operand.Register) op1, op2));
                }
                return;
            case ashr:
                op1 = getReg(irOp1);
                op2 = getOperand(irOp2);
                if (op2 instanceof ImmediateInt) {
                    name = BinaryInstruction.Name.srai;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, result, (Codegen.Operand.Register) op1, op2));
                } else {
                    name = BinaryInstruction.Name.sra;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, false, result, (Codegen.Operand.Register) op1, op2));
                }
        }
    }

    @Override
    public void visit(BitCastTo bitCastTo) {
        RegisterVirtual value = curFunction.getRV(bitCastTo.getValue().getName());
        RegisterVirtual result = curFunction.getRV(bitCastTo.getResult().getName());
        curBlock.addInst(new Move(curBlock, result, value));
    }

    @Override
    public void visit(Br br) {
        Codegen.BasicBlock thenBB = curFunction.getBasicBlock(br.getThenBlock().getName());
        if (br.getCond() != null) {
            RegisterVirtual cond = getReg(br.getCond());
            Codegen.BasicBlock elseBB = curFunction.getBasicBlock(br.getElseBlock().getName());
            curBlock.addInst(new Branch(curBlock, Branch.Name.beq, cond, RegisterPhysical.getVR(0), elseBB));
            curBlock.addInst(new Jump(curBlock, thenBB));
        } else {
            curBlock.addInst(new Jump(curBlock, thenBB));
        }
    }

    @Override
    public void visit(Call call) {
        Codegen.Function callee = asmModule.getFunction(call.getFunction().getName());
        ArrayList<IROper> params = call.getParams();

        int num = Math.min(params.size(), 8);
        for (int i = 0; i < num; ++i) {
            IROper parameter = params.get(i);
            if ((parameter instanceof IntegerConstant) && !(((IntegerConstant) parameter).getValue() >= (1 << 11) || ((IntegerConstant) parameter).getValue() < -(1 << 11))) {
                curBlock.addInst(new LoadImmediate(curBlock, RegisterPhysical.getVR(10 + i), new ImmediateInt(((IntegerConstant) parameter).getValue())));
            } else {
                RegisterVirtual param = getReg(parameter);
                curBlock.addInst(new Move(curBlock, RegisterPhysical.getVR(10 + i), param));
            }
        }

        Stack stack = curFunction.getStack();
        ArrayList<Addr> addrList = stack.getAddrList(callee);
        if (addrList != null) {
            for (int i = 8; i < params.size(); ++i) {
                RegisterVirtual param = getReg(params.get(i));
                curBlock.addInst(new Codegen.Instruction.Store(curBlock, Codegen.Instruction.Store.Name.sw, param, addrList.get(i - 8)));
            }
        } else {
            addrList = new ArrayList<>();
            for (int i = 8; i < params.size(); ++i) {
                RegisterVirtual param = getReg(params.get(i));
                assert param != null;
                Addr addr = new Addr(true, new RegisterVirtual(param.getName() + "_addr"), null);
                addrList.add(addr);
                curBlock.addInst(new Codegen.Instruction.Store(curBlock, Codegen.Instruction.Store.Name.sw, param, addr));
            }
            stack.addAddrList(callee, addrList);
        }

        curBlock.addInst(new Codegen.Instruction.Call(curBlock, callee, num));

        if (call.getResult() != null) {
            RegisterVirtual registerVirtual = curFunction.getRV(call.getResult().getName());
            curBlock.addInst(new Move(curBlock, registerVirtual, RegisterPhysical.getVR(10)));
        }
    }

    @Override
    public void visit(GetElementPtr getElementPtr) {
        RegisterVirtual rd = curFunction.getRV(getElementPtr.getResult().getName());
        IROper irPointer = getElementPtr.getPointer();
        ArrayList<IROper> irIndex = getElementPtr.getIdxes();
        if (irPointer instanceof GlobalVariable) {
            curBlock.addInst(new LoadAddress(curBlock, rd, asmModule.getGlobalVar(irPointer.getName())));
        } else if (irIndex.size() == 1) {
            IROper index = irIndex.get(0);
            RegisterVirtual ptr = curFunction.getRV(irPointer.getName());
            RegisterVirtual rs1 = getReg(index);
            RegisterVirtual tmp = new RegisterVirtual("tmp");
            curFunction.CheckAndSetName(tmp.getName(), tmp);
            RegisterVirtual byteSize = getReg(new IntegerConstant(((PointerType) irPointer.getType()).getType().getByte()));
            curBlock.addInst(new BinaryInstruction(curBlock, BinaryInstruction.Name.mul, false, tmp, rs1, byteSize));
            curBlock.addInst(new BinaryInstruction(curBlock, BinaryInstruction.Name.add, false, rd, ptr, tmp));
        } else {
            assert irPointer.getType() instanceof PointerType;
            IRType baseType = ((PointerType) irPointer.getType()).getType();
            assert baseType instanceof StructureType;
            StructureType type = (StructureType) baseType;
            RegisterVirtual ptr = curFunction.getRV(irPointer.getName());
            int offset = type.getOffset((int) ((IntegerConstant) irIndex.get(1)).getValue());
            Operand rs = getOperand(new IntegerConstant(offset));
            if (rs instanceof Immediate) {
                curBlock.addInst(new BinaryInstruction(curBlock, BinaryInstruction.Name.addi, true, rd, ptr, rs));
            } else {
                curBlock.addInst(new BinaryInstruction(curBlock, BinaryInstruction.Name.add, false, rd, ptr, rs));
            }
        }
    }

    @Override
    public void visit(Icmp icmp) {
        RegisterVirtual rd = curFunction.getRV(icmp.getResult().getName());
        IROper irOp1 = icmp.getOp1();
        IROper irOp2 = icmp.getOp2();
        Icmp.Condition cond = icmp.getCond();

        Operand op1;
        Operand op2;
        BinaryInstruction.Name name;

        RegisterVirtual tmp;

        switch (cond) {
            case slt:
                op2 = getOperand(irOp2);
                op1 = getReg(irOp1);
                if (op2 instanceof Immediate) {
                    name = BinaryInstruction.Name.slti;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, rd, (Codegen.Operand.Register) op1, op2));
                } else {
                    name = BinaryInstruction.Name.slt;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, false, rd, (Codegen.Operand.Register) op1, op2));
                }
                return;
            case sgt:
                op2 = getOperand(irOp1);
                op1 = getReg(irOp2);
                if (op2 instanceof Immediate) {
                    name = BinaryInstruction.Name.slti;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, rd, (Codegen.Operand.Register) op1, op2));
                } else {
                    name = BinaryInstruction.Name.slt;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, false, rd, (Codegen.Operand.Register) op1, op2));
                }
                return;
            case sle:
//                icmp.setForRISCV();
                tmp = new RegisterVirtual("sle_tmp_result");
                curFunction.CheckAndSetName(tmp.getName(), tmp);
                op2 = getOperand(irOp1);
                op1 = getReg(irOp2);
                if (op2 instanceof Immediate) {
                    name = BinaryInstruction.Name.slti;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, tmp, (Codegen.Operand.Register) op1, op2));
                } else {
                    name = BinaryInstruction.Name.slt;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, false, tmp, (Codegen.Operand.Register) op1, op2));
                }
                curBlock.addInst(new BinaryInstruction(curBlock, BinaryInstruction.Name.xori, true, rd, tmp, new ImmediateInt(1)));
                return;
            case sge:
//                icmp.setForRISCV();
                tmp = new RegisterVirtual("sge_tmp_result");
                curFunction.CheckAndSetName(tmp.getName(), tmp);
                op2 = getOperand(irOp2);
                op1 = getReg(irOp1);
                if (op2 instanceof Immediate) {
                    name = BinaryInstruction.Name.slti;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, tmp, (Codegen.Operand.Register) op1, op2));
                } else {
                    name = BinaryInstruction.Name.slt;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, false, tmp, (Codegen.Operand.Register) op1, op2));
                }
                curBlock.addInst(new BinaryInstruction(curBlock, BinaryInstruction.Name.xori, true, rd, tmp, new ImmediateInt(1)));
                return;
            case eq:
                tmp = new RegisterVirtual("tmpResult");
                curFunction.CheckAndSetName(tmp.getName(), tmp);
                op2 = getOperand(irOp2);
                if (op2 instanceof ImmediateInt) {
                    op1 = getReg(irOp1);
                    name = BinaryInstruction.Name.xori;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, tmp, (Codegen.Operand.Register) op1, op2));
                } else {
                    assert op2 instanceof RegisterVirtual;
                    op1 = getOperand(irOp1);
                    if (op1 instanceof ImmediateInt) {
                        name = BinaryInstruction.Name.xori;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, true, tmp, (Codegen.Operand.Register) op2, op1));
                    } else {
                        name = BinaryInstruction.Name.xor;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, false, tmp, (Codegen.Operand.Register) op1, op2));
                    }
                }
                curBlock.addInst(new SetInst(curBlock, SetInst.Name.seqz, rd, tmp));
                return;
            case ne:
                tmp = new RegisterVirtual("tmpResult");
                curFunction.CheckAndSetName(tmp.getName(), tmp);
                op2 = getOperand(irOp2);
                if (op2 instanceof ImmediateInt) {
                    op1 = getReg(irOp1);
                    name = BinaryInstruction.Name.xori;
                    curBlock.addInst(new BinaryInstruction(curBlock, name, true, tmp, (Codegen.Operand.Register) op1, op2));
                } else {
                    assert op2 instanceof RegisterVirtual;
                    op1 = getOperand(irOp1);
                    if (op1 instanceof ImmediateInt) {
                        name = BinaryInstruction.Name.xori;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, true, tmp, (Codegen.Operand.Register) op2, op1));
                    } else {
                        name = BinaryInstruction.Name.xor;
                        curBlock.addInst(new BinaryInstruction(curBlock, name, false, tmp, (Codegen.Operand.Register) op1, op2));
                    }
                }
                curBlock.addInst(new SetInst(curBlock, SetInst.Name.snez, rd, tmp));
        }
    }

    @Override
    public void visit(Load load) {
        RegisterVirtual rd = curFunction.getRV(load.getResult().getName());
        LoadGlobal.Name name = (load.getType().getByte() == 1) ? LoadGlobal.Name.lb : LoadGlobal.Name.lw;

        if (load.getPointer() instanceof GlobalVariable) {
            GlobalVar ptr = asmModule.getGlobalVar(load.getPointer().getName());
            RegisterVirtual luiTmp = new RegisterVirtual("luiTmp");
            curFunction.CheckAndSetName(luiTmp.getName(), luiTmp);
            curBlock.addInst(new LUI(curBlock, luiTmp, new ImmediateRelocation(ImmediateRelocation.ImmType.hi, ptr)));
            curBlock.addInst(new LoadGlobal(curBlock, name, rd, new Addr(false, luiTmp, new ImmediateRelocation(ImmediateRelocation.ImmType.lo, ptr))));
        } else if (load.getPointer() instanceof NullConstant) {
            curBlock.addInst(new LoadGlobal(curBlock, name, rd, new Addr(false, RegisterPhysical.getVR(0), new ImmediateInt(0))));
        } else {
            RegisterVirtual ptr = curFunction.getRV(load.getPointer().getName());
            curBlock.addInst(new LoadGlobal(curBlock, name, rd, new Addr(false, ptr, new ImmediateInt(0))));
        }
    }

    @Override
    public void visit(Phi phi) {
        // Done.
    }

    @Override
    public void visit(Store store) {
        RegisterVirtual rs = getReg(store.getValue());
        Codegen.Instruction.Store.Name name = store.getValue().getType().getByte() == 1 ? Codegen.Instruction.Store.Name.sb : Codegen.Instruction.Store.Name.sw;
        IROper pointer = store.getPointer();
        if (pointer instanceof GlobalVariable) {
            GlobalVar ptr = asmModule.getGlobalVar(pointer.getName());
            RegisterVirtual luiTmp = new RegisterVirtual("luiTmp");
            curFunction.CheckAndSetName(luiTmp.getName(), luiTmp);
            curBlock.addInst(new LUI(curBlock, luiTmp, new ImmediateRelocation(ImmediateRelocation.ImmType.hi, ptr)));
            curBlock.addInst(new Codegen.Instruction.Store(curBlock, name, rs, new Addr(false, luiTmp, new ImmediateRelocation(ImmediateRelocation.ImmType.lo, ptr))));
        } else if (pointer instanceof NullConstant) {
            assert false;
        } else {
            RegisterVirtual ptr = curFunction.getRV(pointer.getName());
            curBlock.addInst(new Codegen.Instruction.Store(curBlock, name, rs, new Addr(false, ptr, new ImmediateInt(0))));
        }
    }

    @Override
    public void visit(Ret ret) {

        for (int cnt: RegisterPhysical.calleeSaveNum) {
            RegisterVirtual rv = curFunction.getRV("x" + cnt);
            curBlock.addInst(new Move(curBlock, RegisterPhysical.getVR(cnt), rv));
        }

        RegisterVirtual ra = curFunction.getRV("ra.save");
        curBlock.addInst(new Move(curBlock, RegisterPhysical.getVR(1), ra));

        if (ret.getReturnVal() != null) {
            RegisterVirtual reg = getReg(ret.getReturnVal());
            curBlock.addInst(new Move(curBlock, RegisterPhysical.getVR(10), reg));
        }
        Return inst = new Return(curBlock, ret.getReturnVal() != null);
        curBlock.addInst(inst);
    }

    private RegisterVirtual getReg(IROper irOper) {
        if (irOper instanceof BoolConstant) {
            RegisterVirtual registerVirtual;
            if (((BoolConstant) irOper).getValue()) {
                registerVirtual = new RegisterVirtual("trueConst");
                curFunction.CheckAndSetName(registerVirtual.getName(), registerVirtual);
                curBlock.addInst(new BinaryInstruction(curBlock, BinaryInstruction.Name.addi, true,
                        registerVirtual, RegisterPhysical.getVR(0), new ImmediateInt(1)));
            } else {
                registerVirtual = RegisterPhysical.getVR(0);
            }
            return registerVirtual;
        } else if (irOper instanceof GlobalVariable) {
            assert false;
            return null;
        } else if (irOper instanceof IntegerConstant) {
            RegisterVirtual registerVirtual;
            if (((IntegerConstant) irOper).getValue() != 0) {
                registerVirtual = new RegisterVirtual("IntegerConstant");
                curFunction.CheckAndSetName(registerVirtual.getName(), registerVirtual);
                long val = ((IntegerConstant) irOper).getValue();
                if (val >= (1 << 11) || val < -(1 << 11)) {
                    curBlock.addInst(new LoadImmediate(curBlock, registerVirtual, new ImmediateInt(val)));
                } else {
                    curBlock.addInst(new BinaryInstruction(curBlock, BinaryInstruction.Name.addi, true,
                            registerVirtual, RegisterPhysical.getVR(0), new ImmediateInt(((IntegerConstant) irOper).getValue())));
                }
            } else {
                registerVirtual = RegisterPhysical.getVR(0);
            }
            return registerVirtual;
        } else if (irOper instanceof NullConstant) {
            return RegisterPhysical.getVR(0);
        } else if (irOper instanceof Parameter) {
            return curFunction.getRV(((Parameter) irOper).getName());
        } else if (irOper instanceof Register) {
            return curFunction.getRV(((Register) irOper).getName());
        } else if (irOper instanceof StringConstant) {
            assert false;
            return null;
        }
        assert false;
        return null;
    }

    private Operand getOperand(IROper irOper) {
        if (irOper instanceof BoolConstant) {
            return new ImmediateInt(((BoolConstant) irOper).getValue() ? 1 : 0);
        } else if (irOper instanceof IntegerConstant) {
            long val = ((IntegerConstant) irOper).getValue();
            if (val >= (1 << 11) || val < -(1 << 11)) {
                return getReg(irOper);
            } else {
                return new ImmediateInt(val);
            }
        } else {
            return getReg(irOper);
        }
    }
}
