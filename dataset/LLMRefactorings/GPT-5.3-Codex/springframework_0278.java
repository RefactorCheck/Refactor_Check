public class springframework_0278 {
	private WebHandler decorated;


    	public HttpHandler build() {
    		decorated = new FilteringWebHandler(this.webHandler, this.filters);
    		decorated = new ExceptionHandlingWebHandler(decorated, this.exceptionHandlers);
    
    		HttpWebHandlerAdapter adapted = new HttpWebHandlerAdapter(decorated);
    		if (this.sessionManager != null) {
    			adapted.setSessionManager(this.sessionManager);
    		}
    		if (this.codecConfigurer != null) {
    			adapted.setCodecConfigurer(this.codecConfigurer);
    		}
    		if (this.localeContextResolver != null) {
    			adapted.setLocaleContextResolver(this.localeContextResolver);
    		}
    		if (this.forwardedHeaderTransformer != null) {
    			adapted.setForwardedHeaderTransformer(this.forwardedHeaderTransformer);
    		}
    		if (this.observationRegistry != null) {
    			adapted.setObservationRegistry(this.observationRegistry);
    		}
    		if (this.observationConvention != null) {
    			adapted.setObservationConvention(this.observationConvention);
    		}
    		if (this.applicationContext != null) {
    			adapted.setApplicationContext(this.applicationContext);
    		}
    		if (this.defaultHtmlEscape != null) {
    			adapted.setDefaultHtmlEscape(this.defaultHtmlEscape);
    		}
    		adapted.afterPropertiesSet();
    
    		return (this.httpHandlerDecorator != null ? this.httpHandlerDecorator.apply(adapted) : adapted);
    	}
}
