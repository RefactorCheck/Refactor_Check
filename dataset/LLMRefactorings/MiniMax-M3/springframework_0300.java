public class springframework_0300 {

    public void unbox(Type type) {
        if (type.getSort() == Type.VOID) {
            return;
        }
        Signature sig = getUnboxSignature(type);
        Type t = Constants.TYPE_NUMBER;
        
        if (sig == null) {
            checkcast(type);
            return;
        }
        
        switch (type.getSort()) {
            case Type.CHAR:
                t = Constants.TYPE_CHARACTER;
                break;
            case Type.BOOLEAN:
                t = Constants.TYPE_BOOLEAN;
                break;
        }
        
        checkcast(t);
        invoke_virtual(t, sig);
    }
    
    private Signature getUnboxSignature(Type type) {
        switch (type.getSort()) {
            case Type.CHAR:
                return CHAR_VALUE;
            case Type.BOOLEAN:
                return BOOLEAN_VALUE;
            case Type.DOUBLE:
                return DOUBLE_VALUE;
            case Type.FLOAT:
                return FLOAT_VALUE;
            case Type.LONG:
                return LONG_VALUE;
            case Type.INT:
            case Type.SHORT:
            case Type.BYTE:
                return INT_VALUE;
            default:
                return null;
        }
    }
}
