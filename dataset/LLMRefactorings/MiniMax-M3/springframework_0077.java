public class springframework_0077 {

	public static int getTypeDifferenceWeight(List<TypeDescriptor> paramTypes, List<TypeDescriptor> argTypes) {
		int result = 0;
		for (int i = 0; i < paramTypes.size(); i++) {
			TypeDescriptor paramType = paramTypes.get(i);
			TypeDescriptor argType = (i < argTypes.size() ? argTypes.get(i) : null);
			if (argType == null) {
				if (paramType.isPrimitive()) {
					return Integer.MAX_VALUE;
				}
			}
			else {
				Class<?> paramTypeClazz = paramType.getType();
				if (!ClassUtils.isAssignable(paramTypeClazz, argType.getType())) {
					return Integer.MAX_VALUE;
				}
				if (paramTypeClazz.isPrimitive()) {
					paramTypeClazz = Object.class;
				}
				result = result + getSuperClassWeight(paramTypeClazz, argType.getType());
				if (paramTypeClazz.isInterface()) {
					result = result + 1;
				}
			}
		}
		return result;
	}

	private static int getSuperClassWeight(Class<?> paramTypeClazz, Class<?> argTypeClazz) {
		int weight = 0;
		Class<?> superClass = argTypeClazz.getSuperclass();
		while (superClass != null) {
			if (paramTypeClazz.equals(superClass)) {
				weight = weight + 2;
				superClass = null;
			}
			else if (ClassUtils.isAssignable(paramTypeClazz, superClass)) {
				weight = weight + 2;
				superClass = superClass.getSuperclass();
			}
			else {
				superClass = null;
			}
		}
		return weight;
	}
}
