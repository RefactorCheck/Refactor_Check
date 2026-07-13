public class springframework_0253 {

    	@SuppressWarnings("unchecked")
    	private Object instantiateUserDefinedStrategy(
    			String classNameValue, Class<?> strategyType, @Nullable ClassLoader classLoader) {
    
    		Object result;
    		try {
    			result = ReflectionUtils.accessibleConstructor(ClassUtils.forName(classNameValue, classLoader)).newInstance();
    		}
    		catch (ClassNotFoundException ex) {
    			throw new IllegalArgumentException("Class [" + classNameValue + "] for strategy [" +
    					strategyType.getName() + "] not found", ex);
    		}
    		catch (Throwable ex) {
    			throw new IllegalArgumentException("Unable to instantiate class [" + classNameValue + "] for strategy [" +
    					strategyType.getName() + "]: a zero-argument constructor is required", ex);
    		}
    
    		if (!strategyType.isInstance(result)) {
    			throw new IllegalArgumentException("Provided class name must be an implementation of " + strategyType);
    		}
    		return result;
    	}
}
