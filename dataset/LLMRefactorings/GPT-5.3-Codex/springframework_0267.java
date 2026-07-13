public class springframework_0267 {

    	@Override
    	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    		// Check for existing error attribute first
    		if (DispatcherType.ASYNC == request.getDispatcherType()) {

    			throw new ServletException("Failed to create response content", ((Throwable) request.getAttribute(WRITE_ERROR_ATTRIBUTE_NAME)));
    		}
    
    		// Start async before Read/WriteListener registration
    		AsyncContext asyncContext = request.startAsync();
    		asyncContext.setTimeout(-1);
    
    		ServletServerHttpRequest httpRequest;
    		AsyncListener requestListener;
    		String logPrefix;
    		try {
    			httpRequest = createRequest((HttpServletRequest) request, asyncContext);
    			requestListener = httpRequest.getAsyncListener();
    			logPrefix = httpRequest.getLogPrefix();
    		}
    		catch (URISyntaxException ((Throwable) request.getAttribute(WRITE_ERROR_ATTRIBUTE_NAME))) {
    			if (logger.isDebugEnabled()) {
    				logger.debug("Failed to get request  URL: " + ((Throwable) request.getAttribute(WRITE_ERROR_ATTRIBUTE_NAME)).getMessage());
    			}
    			((HttpServletResponse) response).setStatus(400);
    			asyncContext.complete();
    			return;
    		}
    
    		ServletServerHttpResponse wrappedResponse =
    				createResponse((HttpServletResponse) response, asyncContext, httpRequest);
    		ServerHttpResponse httpResponse = wrappedResponse;
    		AsyncListener responseListener = wrappedResponse.getAsyncListener();
    		if (httpRequest.getMethod() == HttpMethod.HEAD) {
    			httpResponse = new HttpHeadResponseDecorator(httpResponse);
    		}
    
    		AtomicBoolean completionFlag = new AtomicBoolean();
    		HandlerResultSubscriber subscriber = new HandlerResultSubscriber(asyncContext, completionFlag, logPrefix);
    
    		asyncContext.addListener(new HttpHandlerAsyncListener(
    				requestListener, responseListener, subscriber, completionFlag, logPrefix));
    
    		this.httpHandler.handle(httpRequest, httpResponse).subscribe(subscriber);
    	}
}
