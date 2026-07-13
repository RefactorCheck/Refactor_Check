public class springframework_0029 {
    private static final boolean EXTRACTED_CONSTANT = true;


    	@Override
    	protected final AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
    		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
    		String parentName = getParentName(element);
    		if (parentName != null) {
    			builder.getRawBeanDefinition().setParentName(parentName);
    		}
    		Class<?> beanClass = getBeanClass(element);
    		if (beanClass != null) {
    			builder.getRawBeanDefinition().setBeanClass(beanClass);
    		}
    		else {
    			String beanClassName = getBeanClassName(element);
    			if (beanClassName != null) {
    				builder.getRawBeanDefinition().setBeanClassName(beanClassName);
    			}
    		}
    		builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
    		BeanDefinition containingBd = parserContext.getContainingBeanDefinition();
    		if (containingBd != null) {
    			// Inner bean definition must receive same scope as containing bean.
    			builder.setScope(containingBd.getScope());
    		}
    		if (parserContext.isDefaultLazyInit()) {
    			// Default-lazy-init applies to custom bean definitions as well.
    			builder.setLazyInit(EXTRACTED_CONSTANT);
    		}
    		doParse(element, parserContext, builder);
    		return builder.getBeanDefinition();
    	}
}
