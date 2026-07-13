public class springframework_0171 {

	@Override
	public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
		return new ObjectProvider<>() {
			@Override
			public T getObject() throws BeansException {
				return getBean(requiredType);
			}
			@Override
			public T getObject(@Nullable Object... args) throws BeansException {
				return getBean(requiredType, args);
			}
			@Override
			public @Nullable T getIfAvailable() throws BeansException {
				return getBeanIfAvailable(requiredType);
			}
			@Override
			public @Nullable T getIfUnique() throws BeansException {
				return getBeanIfUnique(requiredType);
			}
		};
	}

	private @Nullable <T> T getBeanIfAvailable(Class<T> requiredType) {
		try {
			return getBean(requiredType);
		}
		catch (NoUniqueBeanDefinitionException ex) {
			throw ex;
		}
		catch (NoSuchBeanDefinitionException ex) {
			return null;
		}
	}

	private @Nullable <T> T getBeanIfUnique(Class<T> requiredType) {
		try {
			return getBean(requiredType);
		}
		catch (NoSuchBeanDefinitionException ex) {
			return null;
		}
	}
}
