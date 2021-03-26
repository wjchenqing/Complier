package Codegen.Operand;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegisterPhysical extends Register {
    static public String[] Names = {
            "zero", "ra", "sp", "gp", "tp",
            "t0", "t1", "t2",
            "s0", "s1",
            "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
            "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11",
            "t3", "t4", "t5", "t6"
    };
    static public String[] colors = {
            "ra",                                                           //1         //0
            "t0", "t1", "t2",                                               //5 ~ 7     //1, 2, 3
            "s0", "s1",                                                     //8, 9      //4, 5
            "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",                 //10 ~ 17   //6  ~ 13
            "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11",   //18 ~ 27   //14 ~ 23
            "t3", "t4", "t5", "t6"                                          //28 ~31    //24 ~ 27
    };
    static public Map<String, RegisterPhysical> physicalRegisterSet = new HashMap<>();
    static public Set<RegisterPhysical> colorSet = new HashSet<>();
    static public Map<String, RegisterVirtual> virtualMap = new HashMap<>();

    static {
        for (String name: Names) {
            physicalRegisterSet.put(name, new RegisterPhysical(name));
        }
        for (String name: Names) {
            virtualMap.put(name, new RegisterVirtual(name));
        }
        for (String name: colors) {
            colorSet.add(physicalRegisterSet.get(name));
        }
    }

    public RegisterPhysical(String name) {
        this.name = name;
    }

    static public RegisterVirtual getVR(int n) {
        return virtualMap.get(Names[n]);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String printCode() {
        return name;
    }
}
