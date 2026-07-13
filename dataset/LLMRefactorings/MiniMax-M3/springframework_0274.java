public class springframework_0274 {

	private static final String STATIC_METHOD_FORMAT_ERROR =
			"staticMethod must be a fully qualified class plus method name: " +
			"for example, 'example.MyExampleClass.myExampleMethod'";

	public void prepare() throws ClassNotFoundException, NoSuchMethodException {
		if (this.staticMethod != null) {
			int lastDotIndex = this.staticMethod.lastIndexOf('.');
			if (lastDotIndex == -1 || lastDotIndex == this.staticMethod.length() - 1) {
				throw new IllegalArgumentException(STATIC_METHOD_FORMAT_ERROR);
			}
			String className = this.staticMethod.substring(0, lastDotIndex);
			String methodName = this.staticMethod.substring(lastDotIndex + 1);
			if (this.targetClass == null || !this.targetClass.getName().equals(className)) {
				this.targetClass = resolveClassName(className);
			}
			this.targetMethod = methodName;
		}

		Class<?> targetClass = getTargetClass();
		String targetMethod = getTargetMethod();
		Assert.notNull(targetClass, "Either 'targetClass' or 'targetObject' is required");
		Assert.notNull(targetMethod, "Property 'targetMethod' is required");

		@Nullable Object[] arguments = getArguments();
		Class<?>[] argTypes = new Class<?>[arguments.length];
		for (int i = 0; i < arguments.length; ++i) {
			Object argument = arguments[i];
			argTypes[i] = (argument != null ? argument.getClass() : Object.class);
		}

		// Try to get the exact method first.
		try {
			this.methodObject = targetClass.getMethod(targetMethod, argTypes);
		}
		catch (NoSuchMethodException ex) {
			// Just rethrow exception if we can't get any match.
			this.methodObject = findMatchingMethod();
			if (this.methodObject == null) {
				throw ex;
			}
		}
	}
}
