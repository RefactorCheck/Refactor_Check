public class springframework_0079 {

    	public static Object constructornewInstance(Constructor<?> constructor, Object... arguments) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    		Object result = null;
    		boolean accessibilityChanged = false;
    		RecordedInvocation.Builder builder = RecordedInvocation.of(InstrumentedMethod.CONSTRUCTOR_NEWINSTANCE)
    				.onInstance(constructor).withArguments(arguments);
    		try {
    			if (!Modifier.isPublic(constructor.getModifiers()) ||
    					!Modifier.isPublic(constructor.getDeclaringClass().getModifiers())) {
    				constructor.setAccessible(true);
    				accessibilityChanged = true;
    			}
    			result = constructor.newInstance(arguments);
    		}
    		finally {
    			RecordedInvocationsPublisher.publish(builder.returnValue(result).build());
    			if (accessibilityChanged) {
    				constructor.setAccessible(false);
    			}
    		}
    		return result;
    	}
}
