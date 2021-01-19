package Frontend.Scope;

import Frontend.Entity.Entity;
import Frontend.Type.ClassType2;
import Frontend.Type.Type2;

import java.util.ArrayList;
import java.util.Map;

public class ClassScope extends Scope {
    private Type2 classType2;

    public ClassScope(Scope parentScope, Type2 type2) {
        super(parentScope);
        classType2 = type2;
    }

    @Override
    public boolean inClassScope() {
        return true;
    }

    @Override
    public Type2 getClassType() {
        return classType2;
    }
}
