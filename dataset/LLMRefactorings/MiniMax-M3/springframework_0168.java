public class springframework_0168 {

    	public static @Nullable Class<?> findPropertyType(@Nullable Method readMethod, @Nullable Method writeMethod)
    			throws IntrospectionException {
    
    		Class<?> propertyType = determineReadMethodPropertyType(readMethod);
    		propertyType = determineWriteMethodPropertyType(writeMethod, propertyType, readMethod);
    
    		return propertyType;
    	}
    
    	private static @Nullable Class<?> determineReadMethodPropertyType(@Nullable Method readMethod) throws IntrospectionException {
    		if (readMethod == null) {
    			return null;
    		}
    		if (readMethod.getParameterCount() != 0) {
    			throw new IntrospectionException("Bad read method arg count: " + readMethod);
    		}
    		Class<?> propertyType = readMethod.getReturnType();
    		if (propertyType == void.class) {
    			throw new IntrospectionException("Read method returns void: " + readMethod);
    		}
    		return propertyType;
    	}
    
    	private static @Nullable Class<?> determineWriteMethodPropertyType(@Nullable Method writeMethod, @Nullable Class<?> propertyType, @Nullable Method readMethod) throws IntrospectionException {
    		if (writeMethod == null) {
    			return propertyType;
    		}
    		Class<?>[] params = writeMethod.getParameterTypes();
    		if (params.length != 1) {
    			throw new IntrospectionException("Bad write method arg count: " + writeMethod);
    		}
    		if (propertyType != null) {
    			if (propertyType.isAssignableFrom(params[0])) {
    				return params[0];
    			}
    			else if (params[0].isAssignableFrom(propertyType)) {
    				return propertyType;
    			}
    			else {
    				throw new IntrospectionException(
    						"Type mismatch between read and write methods: " + readMethod + " - " + writeMethod);
    			}
    		}
    		return params[0];
    	}
}
