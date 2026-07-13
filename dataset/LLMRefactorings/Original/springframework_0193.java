public class springframework_0193 {

    	@SuppressWarnings("removal")
    	private RuntimeBeanReference registerMessageConverter(
    			Element element, ParserContext context, @Nullable Object source) {
    
    		Element convertersElement = DomUtils.getChildElementByTagName(element, "message-converters");
    		ManagedList<Object> converters = new ManagedList<>();
    		if (convertersElement != null) {
    			converters.setSource(source);
    			for (Element beanElement : DomUtils.getChildElementsByTagName(convertersElement, "bean", "ref")) {
    				Object object = context.getDelegate().parsePropertySubElement(beanElement, null);
    				converters.add(object);
    			}
    		}
    		if (convertersElement == null || Boolean.parseBoolean(convertersElement.getAttribute("register-defaults"))) {
    			converters.setSource(source);
    			converters.add(new RootBeanDefinition(StringMessageConverter.class));
    			converters.add(new RootBeanDefinition(ByteArrayMessageConverter.class));
    			if (JACKSON_PRESENT) {
    				RootBeanDefinition jacksonConverterDef = new RootBeanDefinition(JacksonJsonMessageConverter.class);
    				RootBeanDefinition resolverDef = new RootBeanDefinition(DefaultContentTypeResolver.class);
    				resolverDef.getPropertyValues().add("defaultMimeType", MimeTypeUtils.APPLICATION_JSON);
    				jacksonConverterDef.getPropertyValues().add("contentTypeResolver", resolverDef);
    				converters.add(jacksonConverterDef);
    			}
    			else if (JACKSON_2_PRESENT) {
    				RootBeanDefinition jacksonConverterDef = new RootBeanDefinition(MappingJackson2MessageConverter.class);
    				RootBeanDefinition resolverDef = new RootBeanDefinition(DefaultContentTypeResolver.class);
    				resolverDef.getPropertyValues().add("defaultMimeType", MimeTypeUtils.APPLICATION_JSON);
    				jacksonConverterDef.getPropertyValues().add("contentTypeResolver", resolverDef);
    				// Use Jackson factory in order to have well known modules registered automatically
    				GenericBeanDefinition jacksonFactoryDef = new GenericBeanDefinition();
    				jacksonFactoryDef.setBeanClass(Jackson2ObjectMapperFactoryBean.class);
    				jacksonFactoryDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    				jacksonFactoryDef.setSource(source);
    				jacksonConverterDef.getPropertyValues().add("objectMapper", jacksonFactoryDef);
    				converters.add(jacksonConverterDef);
    			}
    			else if (GSON_PRESENT) {
    				converters.add(new RootBeanDefinition(GsonMessageConverter.class));
    			}
    			else if (JSONB_PRESENT) {
    				converters.add(new RootBeanDefinition(JsonbMessageConverter.class));
    			}
    		}
    		ConstructorArgumentValues cargs = new ConstructorArgumentValues();
    		cargs.addIndexedArgumentValue(0, converters);
    		RootBeanDefinition messageConverterDef = new RootBeanDefinition(CompositeMessageConverter.class, cargs, null);
    		String name = MESSAGE_CONVERTER_BEAN_NAME;
    		registerBeanDefByName(name, messageConverterDef, context, source);
    		return new RuntimeBeanReference(name);
    	}
}
