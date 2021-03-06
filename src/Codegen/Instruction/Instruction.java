package Codegen.Instruction;

import Codegen.BasicBlock;
import Codegen.Operand.RegisterVirtual;

import java.util.HashSet;
import java.util.Set;

abstract public class Instruction {
    protected final BasicBlock basicBlock;
    private Instruction prev = null;
    private Instruction next = null;

    protected Set<RegisterVirtual> def = new HashSet<>();
    protected Set<RegisterVirtual> use = new HashSet<>();

    public void replaceDef(RegisterVirtual old, RegisterVirtual n) {
        def.remove(old);
        def.add(n);
    }

    public void replaceUse(RegisterVirtual old, RegisterVirtual n) {
        use.remove(old);
        use.add(n);
    }

    public void deleteInst() {
        if (prev == null) {
            basicBlock.setHeadInst(next);
            if (next != null){
                next.setPrev(null);
            } else {
                basicBlock.setTailInst(null);
            }
        } else if (next == null) {
            basicBlock.setTailInst(prev);
            prev.setNext(null);
        } else {
            prev.setNext(next);
            next.setPrev(prev);
        }
       --basicBlock.instNum;
    }

    public Set<RegisterVirtual> getDef() {
        return def;
    }

    public Set<RegisterVirtual> getUse() {
        return use;
    }

    public Instruction(BasicBlock basicBlock) {
        this.basicBlock = basicBlock;
    }

    public BasicBlock getBasicBlock() {
        return basicBlock;
    }

    public Instruction getPrev() {
        return prev;
    }

    public Instruction getNext() {
        return next;
    }

    public void setPrev(Instruction prev) {
        this.prev = prev;
    }

    public void setNext(Instruction next) {
        this.next = next;
    }

    abstract public void addToUEVarVarKill();

    public void addInstNext(Instruction instruction) {
        instruction.setPrev(this);
        instruction.setNext(next);
        if (next != null) {
            next.setPrev(instruction);
        } else {
            basicBlock.setTailInst(instruction);
        }
        next = instruction;
        ++basicBlock.instNum;
    }

    public void addInstPrev(Instruction instruction) {
        instruction.setPrev(prev);
        instruction.setNext(this);
        if (prev != null) {
            prev.setNext(instruction);
        } else {
            basicBlock.setHeadInst(instruction);
        }
        prev = instruction;
        ++basicBlock.instNum;
    }

    @Override
    abstract public String toString();

    abstract public String printCode();
}
