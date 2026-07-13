public class springframework_0177 {

	public static Method selectInvocableMethod(Method method, Class<?> targetType) {
		if (method.getDeclaringClass().isAssignableFrom(targetType)) {
			return method;
		}
		try {
			String methodName = method.getName();
			Class<?>[] parameterTypes = method.getParameterTypes();
			Method invocable = findMethodOnInterfaces(targetType, methodName, parameterTypes);
			if (invocable != null) {
				return invocable;
			}
			// A final desperate attempt on the proxy class itself...
			return targetType.getMethod(methodName, parameterTypes);
		}
		catch (NoSuchMethodException ex) {
			throw new IllegalStateException(String.format(
					"Need to invoke method '%s' declared on target class '%s', " +
					"but not found in any interface(s) of the exposed proxy type. " +
					"Either pull the method up to an interface or switch to CGLIB " +
					"proxies by enforcing proxy-target-class mode in your configuration.",
					method.getName(), method.getDeclaringClass().getSimpleName()));
		}
	}

	private static Method findMethodOnInterfaces(Class<?> targetType, String methodName, Class<?>[] parameterTypes) {
		for (Class<?> ifc : targetType.getInterfaces()) {
			try {
				return ifc.getMethod(methodName, parameterTypes);
			}
			catch (NoSuchMethodException ex) {
				// Alright, not on this interface then...
			}
		}
		return null;
	}
}
