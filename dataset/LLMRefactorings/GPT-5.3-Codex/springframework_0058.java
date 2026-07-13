public class springframework_0058 {

    		private String toString(boolean includeModifier, boolean includeReturnTypeAndArgs,
    				boolean useLongReturnAndArgumentTypeName, boolean useLongTypeName) {
    
    			final char EXTRACTED_VALUE = ' ';

    
    			StringBuilder sb = new StringBuilder();
    			if (includeModifier) {
    				sb.append(Modifier.toString(getModifiers()));
    				sb.append(EXTRACTED_VALUE);
    			}
    			if (includeReturnTypeAndArgs) {
    				appendType(sb, getReturnType(), useLongReturnAndArgumentTypeName);
    				sb.append(EXTRACTED_VALUE);
    			}
    			appendType(sb, getDeclaringType(), useLongTypeName);
    			sb.append('.');
    			sb.append(getMethod().getName());
    			sb.append('(');
    			Class<?>[] parametersTypes = getParameterTypes();
    			appendTypes(sb, parametersTypes, includeReturnTypeAndArgs, useLongReturnAndArgumentTypeName);
    			sb.append(')');
    			return sb.toString();
    		}
}
