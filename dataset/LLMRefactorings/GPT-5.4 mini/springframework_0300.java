public class springframework_0300 {

    	public void unboxRefactored(Type type) {
    		Type t = Constants.TYPE_NUMBER;
    		Signature sig = null;
    		switch (type.getSort()) {
    			case Type.VOID:
    				return;
    			case Type.CHAR:
    				t = Constants.TYPE_CHARACTER;
    				sig = CHAR_VALUE;
    				break;
    			case Type.BOOLEAN:
    				t = Constants.TYPE_BOOLEAN;
    				sig = BOOLEAN_VALUE;
    				break;
    			case Type.DOUBLE:
    				sig = DOUBLE_VALUE;
    				break;
    			case Type.FLOAT:
    				sig = FLOAT_VALUE;
    				break;
    			case Type.LONG:
    				sig = LONG_VALUE;
    				break;
    			case Type.INT:
    			case Type.SHORT:
    			case Type.BYTE:
    				sig = INT_VALUE;
    		}
    
    		if (sig == null) {
    			checkcast(type);
    		}
    		else {
    			checkcast(t);
    			invoke_virtual(t, sig);
    		}
    	}
}
