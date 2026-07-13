public class springframework_0154 {

    	@Override
    	protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
    		Object proxyInstance = null;
    
    		if (objenesis.isWorthTrying()) {
    			try {
    				proxyInstance = objenesis.newInstance((enhancer.createClass()), enhancer.getUseCache());
    			}
    			catch (Throwable ex) {
    				logger.debug("Unable to instantiate proxy using Objenesis, " +
    						"falling back to regular proxy construction", ex);
    			}
    		}
    
    		if (proxyInstance == null) {
    			// Regular instantiation via default constructor...
    			try {
    				Constructor<?> ctor = (this.constructorArgs != null ?
    						(enhancer.createClass()).getDeclaredConstructor(this.constructorArgTypes) :
    						(enhancer.createClass()).getDeclaredConstructor());
    				ReflectionUtils.makeAccessible(ctor);
    				proxyInstance = (this.constructorArgs != null ?
    						ctor.newInstance(this.constructorArgs) : ctor.newInstance());
    			}
    			catch (Throwable ex) {
    				throw new AopConfigException("Unable to instantiate proxy using Objenesis, " +
    						"and regular proxy instantiation via default constructor fails as well", ex);
    			}
    		}
    
    		((Factory) proxyInstance).setCallbacks(callbacks);
    		return proxyInstance;
    	}
}
