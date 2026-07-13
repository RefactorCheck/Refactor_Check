public class springframework_0270 {

    private ContentNegotiatingViewResolver initContentNegotiatingViewResolver(View[] defaultViews) {
        // ContentNegotiatingResolver in the registry: elevate its precedence!
        this.order = (this.order != null ? this.order : Ordered.HIGHEST_PRECEDENCE);

        if (this.contentNegotiatingResolver != null) {
            if (!ObjectUtils.isEmpty(defaultViews) &&
                    !CollectionUtils.isEmpty(this.contentNegotiatingResolver.getDefaultViews())) {
                List<View> views = new ArrayList<>(this.contentNegotiatingResolver.getDefaultViews());
                views.addAll(Arrays.asList(defaultViews));
                this.contentNegotiatingResolver.setDefaultViews(views);
            }
        }
        else {
            this.contentNegotiatingResolver = createContentNegotiatingViewResolver(defaultViews);
        }
        return this.contentNegotiatingResolver;
    }

    private ContentNegotiatingViewResolver createContentNegotiatingViewResolver(View[] defaultViews) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setDefaultViews(Arrays.asList(defaultViews));
        resolver.setViewResolvers(this.viewResolvers);
        if (this.contentNegotiationManager != null) {
            resolver.setContentNegotiationManager(this.contentNegotiationManager);
        }
        return resolver;
    }
}
