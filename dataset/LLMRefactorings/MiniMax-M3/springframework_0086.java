public class springframework_0086 {

    private static final String REDIRECT_URL_ATTRIBUTE = "redirect-url";
    private static final String CONTEXT_RELATIVE_ATTRIBUTE = "context-relative";
    private static final String KEEP_QUERY_PARAMS_ATTRIBUTE = "keep-query-params";
    private static final String STATUS_CODE_PROPERTY = "statusCode";
    private static final String CONTEXT_RELATIVE_PROPERTY = "contextRelative";
    private static final String PROPAGATE_QUERY_PARAMS_PROPERTY = "propagateQueryParams";

    private RootBeanDefinition getRedirectView(Element element, @Nullable HttpStatusCode status, @Nullable Object source) {
        RootBeanDefinition redirectView = new RootBeanDefinition(RedirectView.class);
        redirectView.setSource(source);
        redirectView.getConstructorArgumentValues().addIndexedArgumentValue(0, element.getAttribute(REDIRECT_URL_ATTRIBUTE));

        if (status != null) {
            redirectView.getPropertyValues().add(STATUS_CODE_PROPERTY, status);
        }

        if (element.hasAttribute(CONTEXT_RELATIVE_ATTRIBUTE)) {
            redirectView.getPropertyValues().add(CONTEXT_RELATIVE_PROPERTY, element.getAttribute(CONTEXT_RELATIVE_ATTRIBUTE));
        }
        else {
            redirectView.getPropertyValues().add(CONTEXT_RELATIVE_PROPERTY, true);
        }

        if (element.hasAttribute(KEEP_QUERY_PARAMS_ATTRIBUTE)) {
            redirectView.getPropertyValues().add(PROPAGATE_QUERY_PARAMS_PROPERTY, element.getAttribute(KEEP_QUERY_PARAMS_ATTRIBUTE));
        }

        return redirectView;
    }
}
