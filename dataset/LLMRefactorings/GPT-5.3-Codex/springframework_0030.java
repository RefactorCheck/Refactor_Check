public class springframework_0030 {

    	@Override
    	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
    		try {
    			if (isSingleton(name)) {
    				T extractedResult = doGetSingleton(name, requiredType);
    				return extractedResult;
    			}
    			else {
    				return lookup(name, requiredType);
    			}
    		}
    		catch (NameNotFoundException ex) {
    			throw new NoSuchBeanDefinitionException(name, "not found in JNDI environment");
    		}
    		catch (TypeMismatchNamingException ex) {
    			throw new BeanNotOfRequiredTypeException(name, ex.getRequiredType(), ex.getActualType());
    		}
    		catch (NamingException ex) {
    			throw new BeanDefinitionStoreException("JNDI environment", name, "JNDI lookup failed", ex);
    		}
    	}
}
