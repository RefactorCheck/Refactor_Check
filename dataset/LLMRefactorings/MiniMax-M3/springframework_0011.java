public class springframework_0011 {

		@Override
		public void addMapping(Element element, ManagedMap<String, Object> urlMap, ParserContext context) {
			String pathAttribute = element.getAttribute("path");
			String[] mappings = StringUtils.tokenizeToStringArray(pathAttribute, ",");
			RuntimeBeanReference handlerReference = new RuntimeBeanReference(element.getAttribute("handler"));

			ConstructorArgumentValues cargs = new ConstructorArgumentValues();
			cargs.addIndexedArgumentValue(0, handlerReference);
			cargs.addIndexedArgumentValue(1, this.handshakeHandlerReference);
			RootBeanDefinition requestHandlerDef = new RootBeanDefinition(WebSocketHttpRequestHandler.class, cargs, null);
			Object source = context.extractSource(element);
			requestHandlerDef.setSource(source);
			requestHandlerDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			requestHandlerDef.getPropertyValues().add("handshakeInterceptors", this.interceptorsList);
			String requestHandlerName = context.getReaderContext().registerWithGeneratedName(requestHandlerDef);
			RuntimeBeanReference requestHandlerRef = new RuntimeBeanReference(requestHandlerName);

			for (String mapping : mappings) {
				urlMap.put(mapping, requestHandlerRef);
			}
		}
}
