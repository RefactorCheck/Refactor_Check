public class springframework_0030 {

    	@Override
    	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
    		try {
    			if (isSingleton(name)) {
    				return doGetSingleton(name, requiredType);
    			}
    			else {
    				return lookup(name, requiredType);
    			}
    		}
    		catch (NamingException ex) {
    			throw translateJndiException(name, ex);
    		}
    	}

    	private BeansException translateJndiException(String name, NamingException ex) {
    		if (ex instanceof TypeMismatchNamingException) {
    			TypeMismatchNamingException tmex = (TypeMismatchNamingException) ex;
    			return new BeanNotOfRequiredTypeException(name, tmex.getRequiredType(), tmex.getActualType());
    		}
    		if (ex instanceof NameNotFoundException) {
    			return new NoSuchBeanDefinitionException(name, "not found in JNDI environment");
    		}
    		return new BeanDefinitionStoreException("JNDI environment", name, "JNDI lookup failed", ex);
    	}
}
