public class springframework_0087 {

	private boolean isDeclaredInInterface(Method method, String beanKey) {
		Class<?>[] ifaces = null;

		if (this.resolvedInterfaceMappings != null) {
			ifaces = this.resolvedInterfaceMappings.get(beanKey);
		}

		if (ifaces == null) {
			ifaces = this.managedInterfaces;
			if (ifaces == null) {
				ifaces = ClassUtils.getAllInterfacesForClass(method.getDeclaringClass());
			}
		}

		for (Class<?> ifc : ifaces) {
			for (Method ifcMethod : ifc.getMethods()) {
				if (isMatchingMethod(ifcMethod, method)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isMatchingMethod(Method ifcMethod, Method method) {
		return ifcMethod.getName().equals(method.getName()) &&
				ifcMethod.getParameterCount() == method.getParameterCount() &&
				Arrays.equals(ifcMethod.getParameterTypes(), method.getParameterTypes());
	}
}
