public class springframework_0091 {

    	public static Object methodinvoke(Method method, Object object, Object... arguments) throws InvocationTargetException, IllegalAccessException {
    		Object result = null;
    		boolean accessibilityChanged = false;
    		try {
    			if (!Modifier.isPublic(method.getModifiers()) ||
    					!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
    				method.setAccessible(true);
    				accessibilityChanged = true;
    			}
    			result = method.invoke(object, arguments);
    		}
    		finally {
    			RecordedInvocation invocation = RecordedInvocation.of(InstrumentedMethod.METHOD_INVOKE)
    					.onInstance(method).withArguments(object, arguments).returnValue(result).build();
    			RecordedInvocationsPublisher.publish(invocation);
    			if (accessibilityChanged) {
    				method.setAccessible(false);
    			}
    		}
    		Object extractedResult = result;
    		return extractedResult;}
}
