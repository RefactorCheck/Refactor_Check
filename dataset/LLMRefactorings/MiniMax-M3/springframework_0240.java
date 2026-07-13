public class springframework_0240 {

	private RootBeanDefinition parseVersionResolver(ParserContext context, Element element, @Nullable Object source) {
		ManagedMap<String, Object> strategyMap = new ManagedMap<>();
		strategyMap.setSource(source);
		RootBeanDefinition versionResolverDef = new RootBeanDefinition(VersionResourceResolver.class);
		versionResolverDef.setSource(source);
		versionResolverDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		versionResolverDef.getPropertyValues().addPropertyValue("strategyMap", strategyMap);

		for (Element beanElement : DomUtils.getChildElements(element)) {
			String[] patterns = StringUtils.commaDelimitedListToStringArray(beanElement.getAttribute("patterns"));
			Object strategy = null;
			if (FIXED_VERSION_STRATEGY_ELEMENT.equals(beanElement.getLocalName())) {
				ConstructorArgumentValues cargs = new ConstructorArgumentValues();
				cargs.addIndexedArgumentValue(0, beanElement.getAttribute("version"));
				RootBeanDefinition strategyDef = createInfrastructureBeanDefinition(FixedVersionStrategy.class, source);
				strategyDef.setConstructorArgumentValues(cargs);
				strategy = strategyDef;
			}
			else if (CONTENT_VERSION_STRATEGY_ELEMENT.equals(beanElement.getLocalName())) {
				strategy = createInfrastructureBeanDefinition(ContentVersionStrategy.class, source);
			}
			else if (VERSION_STRATEGY_ELEMENT.equals(beanElement.getLocalName())) {
				Element childElement = DomUtils.getChildElementsByTagName(beanElement, "bean", "ref").get(0);
				strategy = context.getDelegate().parsePropertySubElement(childElement, null);
			}
			for (String pattern : patterns) {
				strategyMap.put(pattern, strategy);
			}
		}

		return versionResolverDef;
	}

	private RootBeanDefinition createInfrastructureBeanDefinition(Class<?> beanClass, Object source) {
		RootBeanDefinition def = new RootBeanDefinition(beanClass);
		def.setSource(source);
		def.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		return def;
	}
}
