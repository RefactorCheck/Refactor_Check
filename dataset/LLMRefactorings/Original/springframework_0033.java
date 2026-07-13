public class springframework_0033 {

    	@Override
    	public List<Advisor> getAdvisors(MetadataAwareAspectInstanceFactory aspectInstanceFactory) {
    		Class<?> aspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
    		String aspectName = aspectInstanceFactory.getAspectMetadata().getAspectName();
    		validate(aspectClass);
    
    		// We need to wrap the MetadataAwareAspectInstanceFactory with a decorator
    		// so that it will only instantiate once.
    		MetadataAwareAspectInstanceFactory lazySingletonAspectInstanceFactory =
    				new LazySingletonAspectInstanceFactoryDecorator(aspectInstanceFactory);
    
    		List<Advisor> advisors = new ArrayList<>();
    		for (Method method : getAdvisorMethods(aspectClass)) {
    			if (method.equals(ClassUtils.getMostSpecificMethod(method, aspectClass))) {
    				// Prior to Spring Framework 5.2.7, advisors.size() was supplied as the declarationOrderInAspect
    				// to getAdvisor(...) to represent the "current position" in the declared methods list.
    				// However, since Java 7 the "current position" is not valid since the JDK no longer
    				// returns declared methods in the order in which they are declared in the source code.
    				// Thus, we now hard code the declarationOrderInAspect to 0 for all advice methods
    				// discovered via reflection in order to support reliable advice ordering across JVM launches.
    				// Specifically, a value of 0 aligns with the default value used in
    				// AspectJPrecedenceComparator.getAspectDeclarationOrder(Advisor).
    				Advisor advisor = getAdvisor(method, lazySingletonAspectInstanceFactory, 0, aspectName);
    				if (advisor != null) {
    					advisors.add(advisor);
    				}
    			}
    		}
    
    		// If it's a per target aspect, emit the dummy instantiating aspect.
    		if (!advisors.isEmpty() && lazySingletonAspectInstanceFactory.getAspectMetadata().isLazilyInstantiated()) {
    			Advisor instantiationAdvisor = new SyntheticInstantiationAdvisor(lazySingletonAspectInstanceFactory);
    			advisors.add(0, instantiationAdvisor);
    		}
    
    		// Find introduction fields.
    		for (Field field : aspectClass.getDeclaredFields()) {
    			Advisor advisor = getDeclareParentsAdvisor(field);
    			if (advisor != null) {
    				advisors.add(advisor);
    			}
    		}
    
    		return advisors;
    	}
}
