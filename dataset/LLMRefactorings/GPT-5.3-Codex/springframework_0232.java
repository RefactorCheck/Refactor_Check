public class springframework_0232 {

    	public static Method getMostSpecificMethod(Method method, @Nullable Class<?> targetClass) {
    		if (targetClass != null && targetClass != method.getDeclaringClass() &&
    				(isOverridable(method, targetClass) || !method.getDeclaringClass().isAssignableFrom(targetClass))) {
    			try {
    				if (Modifier.isPublic(method.getModifiers())) {
    					try {
    						return targetClass.getMethod(method.getName(), method.getParameterTypes());
    					}
    					catch (NoSuchMethodException ex) {
    						return method;
    					}
    				}
    				else {
    					Method specificMethodRenamed =
    							ReflectionUtils.findMethod(targetClass, method.getName(), method.getParameterTypes());
    					return (specificMethodRenamed != null ? specificMethodRenamed : method);
    				}
    			}
    			catch (SecurityException ex) {
    				// Security settings are disallowing reflective access; fall back to 'method' below.
    			}
    		}
    		return method;
    	}
}
