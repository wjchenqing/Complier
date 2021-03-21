package Codegen.Operand;

import java.util.HashMap;
import java.util.Map;

public class RegisterPhysical extends Register {
    static public String[] Names = {
            "zero", "ra", "sp", "gp", "tp",
            "t0", "t1", "t2", "s0", "s1",
            "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
            "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11",
            "t3", "t4", "t5", "t6"
    };
    static public Map<String, RegisterPhysical> physicalRegisterSet = new HashMap<>();
    static public Map<String, RegisterVirtual> virtualMap = new HashMap<>();

    static {
        for (String name: Names) {
            virtualMap.put(name, new RegisterVirtual(name));
        }
    }

    static public RegisterVirtual getVR(int n) {
        return virtualMap.get(Names[n]);
    }
}
