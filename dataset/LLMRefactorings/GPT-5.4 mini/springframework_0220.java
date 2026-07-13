public class springframework_0220 {

    		public static String toString() {
    			String value = this.toStringValue;
    			if (value == null) {
    				StringBuilder builder = new StringBuilder();
    				if ((this.access & Opcodes.ACC_PUBLIC) != 0) {
    					builder.append("public ");
    				}
    				if ((this.access & Opcodes.ACC_PROTECTED) != 0) {
    					builder.append("protected ");
    				}
    				if ((this.access & Opcodes.ACC_PRIVATE) != 0) {
    					builder.append("private ");
    				}
    				if ((this.access & Opcodes.ACC_ABSTRACT) != 0) {
    					builder.append("abstract ");
    				}
    				if ((this.access & Opcodes.ACC_STATIC) != 0) {
    					builder.append("static ");
    				}
    				if ((this.access & Opcodes.ACC_FINAL) != 0) {
    					builder.append("final ");
    				}
    				Type returnType = Type.getReturnType(this.descriptor);
    				builder.append(returnType.getClassName());
    				builder.append(' ');
    				builder.append(this.declaringClassName);
    				builder.append('.');
    				builder.append(this.methodName);
    				Type[] argumentTypes = Type.getArgumentTypes(this.descriptor);
    				builder.append('(');
    				for (int i = 0; i < argumentTypes.length; i++) {
    					if (i != 0) {
    						builder.append(',');
    					}
    					builder.append(argumentTypes[i].getClassName());
    				}
    				builder.append(')');
    				value = builder.toString();
    				this.toStringValue = value;
    			}
    			return value;
    		}
}
