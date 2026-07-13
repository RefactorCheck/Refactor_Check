public class springframework_0086 {

    	private RootBeanDefinition getRedirectView(Element element, @Nullable HttpStatusCode status, @Nullable Object source) {

    		(new RootBeanDefinition(RedirectView.class)).setSource(source);
    		(new RootBeanDefinition(RedirectView.class)).getConstructorArgumentValues().addIndexedArgumentValue(0, element.getAttribute("redirect-url"));
    
    		if (status != null) {
    			(new RootBeanDefinition(RedirectView.class)).getPropertyValues().add("statusCode", status);
    		}
    
    		if (element.hasAttribute("context-relative")) {
    			(new RootBeanDefinition(RedirectView.class)).getPropertyValues().add("contextRelative", element.getAttribute("context-relative"));
    		}
    		else {
    			(new RootBeanDefinition(RedirectView.class)).getPropertyValues().add("contextRelative", true);
    		}
    
    		if (element.hasAttribute("keep-query-params")) {
    			(new RootBeanDefinition(RedirectView.class)).getPropertyValues().add("propagateQueryParams", element.getAttribute("keep-query-params"));
    		}
    
    		return (new RootBeanDefinition(RedirectView.class));
    	}
}
