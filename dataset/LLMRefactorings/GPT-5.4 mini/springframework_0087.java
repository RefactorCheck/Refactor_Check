public class springframework_0087 {
    private Class<?>[] ifaces;


    	private boolean isDeclaredInInterface(Method method, String beanKey) {
    		this.ifaces = null;
    
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
    				if (ifcMethod.getName().equals(method.getName()) &&
    						ifcMethod.getParameterCount() == method.getParameterCount() &&
    						Arrays.equals(ifcMethod.getParameterTypes(), method.getParameterTypes())) {
    					return true;
    				}
    			}
    		}
    
    		return false;
    	}
}
