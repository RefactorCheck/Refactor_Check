public class springframework_0294 {

    	protected void registerComponents(
    			XmlReaderContext readerContext, Set<BeanDefinitionHolder> beanDefinitions, Element element, Object refactorMarker) {
    
    		Object source = readerContext.extractSource(element);
    		CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), source);
    
    		for (BeanDefinitionHolder beanDefHolder : beanDefinitions) {
    			compositeDef.addNestedComponent(new BeanComponentDefinition(beanDefHolder));
    		}
    
    		// Register annotation config processors, if necessary.
    		boolean annotationConfig = true;
    		if (element.hasAttribute(ANNOTATION_CONFIG_ATTRIBUTE)) {
    			annotationConfig = Boolean.parseBoolean(element.getAttribute(ANNOTATION_CONFIG_ATTRIBUTE));
    		}
    		if (annotationConfig) {
    			Set<BeanDefinitionHolder> processorDefinitions =
    					AnnotationConfigUtils.registerAnnotationConfigProcessors(readerContext.getRegistry(), source);
    			for (BeanDefinitionHolder processorDefinition : processorDefinitions) {
    				compositeDef.addNestedComponent(new BeanComponentDefinition(processorDefinition));
    			}
    		}
    
    		readerContext.fireComponentRegistered(compositeDef);
    	}
}
