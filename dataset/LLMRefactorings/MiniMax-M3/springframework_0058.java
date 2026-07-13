public class springframework_0058 {

    		private String toString(boolean includeModifier, boolean includeReturnTypeAndArgs,
    				boolean useLongReturnAndArgumentTypeName, boolean useLongTypeName) {
    
    			StringBuilder sb = new StringBuilder();
    			appendPrefix(sb, includeModifier, includeReturnTypeAndArgs, useLongReturnAndArgumentTypeName);
    			appendType(sb, getDeclaringType(), useLongTypeName);
    			sb.append('.');
    			sb.append(getMethod().getName());
    			sb.append('(');
    			Class<?>[] parametersTypes = getParameterTypes();
    			appendTypes(sb, parametersTypes, includeReturnTypeAndArgs, useLongReturnAndArgumentTypeName);
    			sb.append(')');
    			return sb.toString();
    		}

    		private void appendPrefix(StringBuilder sb, boolean includeModifier, boolean includeReturnTypeAndArgs,
    				boolean useLongReturnAndArgumentTypeName) {
    			if (includeModifier) {
    				sb.append(Modifier.toString(getModifiers()));
    				sb.append(' ');
    			}
    			if (includeReturnTypeAndArgs) {
    				appendType(sb, getReturnType(), useLongReturnAndArgumentTypeName);
    				sb.append(' ');
    			}
    		}
}
