public class springframework_0182 {

    	public static MethodProxy create(Class c1, Class c2, String desc, String name1, String name2) {
    		MethodProxy proxy = new MethodProxy();
    		proxy.sig1 = new Signature(name1, desc);
    		proxy.sig2 = new Signature(name2, desc);
    		proxy.createInfo = new CreateInfo(c1, c2);
    
    		// SPRING PATCH BEGIN
    		if (c1 != Object.class && c1.isAssignableFrom(c2.getSuperclass()) && !Factory.class.isAssignableFrom(c2)) {
    			// Try early initialization for overridden methods on specifically purposed subclasses
    			try {
    				proxy.init();
    			}
    			catch (CodeGenerationException ignored) {
    				// to be retried when actually needed later on (possibly not at all)
    			}
    		}
    		// SPRING PATCH END
    
    		return proxy;
    	}
}
