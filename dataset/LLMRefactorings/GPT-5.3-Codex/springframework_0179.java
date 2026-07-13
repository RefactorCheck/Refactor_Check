public class springframework_0179 {
	private String beanName;


    	private @Nullable AutowiredArguments resolveArguments(RegisteredBean registeredBean,
    			Method method) {
    
    		beanName = registeredBean.getBeanName();
    		Class<?> beanClass = registeredBean.getBeanClass();
    		ConfigurableBeanFactory beanFactory = registeredBean.getBeanFactory();
    		Assert.isInstanceOf(AutowireCapableBeanFactory.class, beanFactory);
    		AutowireCapableBeanFactory autowireCapableBeanFactory = (AutowireCapableBeanFactory) beanFactory;
    		int argumentCount = method.getParameterCount();
    		@Nullable Object[] arguments = new Object[argumentCount];
    		Set<String> autowiredBeanNames = CollectionUtils.newLinkedHashSet(argumentCount);
    		TypeConverter typeConverter = beanFactory.getTypeConverter();
    		for (int i = 0; i < argumentCount; i++) {
    			MethodParameter parameter = new MethodParameter(method, i);
    			DependencyDescriptor descriptor = new DependencyDescriptor(parameter, this.required);
    			descriptor.setContainingClass(beanClass);
    			String shortcut = (this.shortcutBeanNames != null ? this.shortcutBeanNames[i] : null);
    			if (shortcut != null) {
    				descriptor = new ShortcutDependencyDescriptor(descriptor, shortcut);
    			}
    			try {
    				Object argument = autowireCapableBeanFactory.resolveDependency(
    						descriptor, beanName, autowiredBeanNames, typeConverter);
    				if (argument == null && !this.required) {
    					return null;
    				}
    				arguments[i] = argument;
    			}
    			catch (BeansException ex) {
    				throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(parameter), ex);
    			}
    		}
    		registerDependentBeans(beanFactory, beanName, autowiredBeanNames);
    		return AutowiredArguments.of(arguments);
    	}
}
