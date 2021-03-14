package IR.Type;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class StructureType extends IRType {
    private final String structureName;
    private ArrayList<IRType> typeList = new ArrayList<>();

    public StructureType(String structureName) {
        this.structureName = structureName;
    }

    public StructureType(String structureName, ArrayList<IRType> typeList) {
        this.structureName = structureName;
        this.typeList = typeList;
    }

    public String getStructureName() {
        return structureName;
    }

    public ArrayList<IRType> getTypeList() {
        return typeList;
    }

    public void putIRType (IRType type) {
        typeList.add(type);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("%").append(structureName).append(" = type { ");
        int bound = typeList.size();
        AtomicInteger i = new AtomicInteger(1);
        for (IRType type: typeList) {
            stringBuilder.append(type.toString());
            if (i.get() != bound) {
                stringBuilder.append(", ");
            }
            i.incrementAndGet();
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof StructureType) && (structureName.equals(((StructureType) obj).structureName));
    }

    @Override
    public int getByte() {
        int ans = 0;
        int max = 0;
        for (IRType type: typeList) {
            int typeByte = type.getByte();
            if (ans == 0) {
                ans = typeByte;
            } else if (ans % typeByte == 0) {
                ans += typeByte;
            } else {
                ans = ans + (typeByte - ans % typeByte) + typeByte;
            }
            max = Math.max(max, typeByte);
        }
        if (ans % max == 0) {
            ans += max;
        } else {
            ans = ans + (max - ans % max) + max;
        }
        return 0;
    }
}
