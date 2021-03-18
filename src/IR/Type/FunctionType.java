package IR.Type;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class FunctionType extends IRType {
    private final IRType returnType;
    private final ArrayList<IRType> paramTypeList;

    public FunctionType(IRType returnType, ArrayList<IRType> paramTypeList) {
        this.returnType = returnType;
        this.paramTypeList = paramTypeList;
    }

    public IRType getReturnType() {
        return returnType;
    }

    public ArrayList<IRType> getParamTypeList() {
        return paramTypeList;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(returnType.toString()).append(" (");
        int bound = paramTypeList.size();
        AtomicInteger i = new AtomicInteger(1);
        for (IRType paramType: paramTypeList) {
            string.append(paramType.toString());
            if (i.get() != bound) {
                string.append(", ");
            }
            i.incrementAndGet();
        }
        string.append(")\n");
        return string.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof FunctionType)) {
            return false;
        } else if (!returnType.equals(((FunctionType) obj).returnType)) {
            return false;
        } else if (paramTypeList.size() != ((FunctionType) obj).paramTypeList.size()) {
            return false;
        } else {
            return IntStream.range(0, paramTypeList.size()).allMatch(i
                    -> ((FunctionType) obj).paramTypeList.get(i).equals(paramTypeList.get(i)));
        }
    }

    @Override
    public int getByte() {
        assert false;
        return 0;
    }
}
