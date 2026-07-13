public class springframework_0297 {

	@Override
	protected RootBeanDefinition createContainer(Element containerEle, Element listenerEle, ParserContext parserContext,
			PropertyValues commonContainerProperties, PropertyValues specificContainerProperties) {

		RootBeanDefinition containerDef = new RootBeanDefinition();
		containerDef.setSource(parserContext.extractSource(containerEle));
		containerDef.getPropertyValues().addPropertyValues(commonContainerProperties);
		containerDef.getPropertyValues().addPropertyValues(specificContainerProperties);

		String containerType = containerEle.getAttribute(CONTAINER_TYPE_ATTRIBUTE);
		String containerClass = containerEle.getAttribute(CONTAINER_CLASS_ATTRIBUTE);
		String beanClassName = resolveContainerBeanClassName(containerClass, containerType, containerEle, parserContext);
		if (beanClassName != null) {
			containerDef.setBeanClassName(beanClassName);
		}

		// Parse listener specific settings
		parseListenerConfiguration(listenerEle, parserContext, containerDef.getPropertyValues());

		return containerDef;
	}

	private String resolveContainerBeanClassName(String containerClass, String containerType,
			Element containerEle, ParserContext parserContext) {
		if (StringUtils.hasLength(containerClass)) {
			return containerClass;
		}
		else if (!StringUtils.hasLength(containerType) || containerType.startsWith("default")) {
			return "org.springframework.jms.listener.DefaultMessageListenerContainer";
		}
		else if (containerType.startsWith("simple")) {
			return "org.springframework.jms.listener.SimpleMessageListenerContainer";
		}
		else {
			parserContext.getReaderContext().error(
					"Invalid 'container-type' attribute: only \"default\" and \"simple\" supported.", containerEle);
			return null;
		}
	}
}
