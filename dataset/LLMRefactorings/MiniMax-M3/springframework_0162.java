public class springframework_0162 {

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		if (!StringUtils.hasText(this.defaultServletName)) {
			this.defaultServletName = determineDefaultServletName();
		}
	}

	private String determineDefaultServletName() {
		if (this.servletContext.getNamedDispatcher(COMMON_DEFAULT_SERVLET_NAME) != null) {
			return COMMON_DEFAULT_SERVLET_NAME;
		}
		else if (this.servletContext.getNamedDispatcher(GAE_DEFAULT_SERVLET_NAME) != null) {
			return GAE_DEFAULT_SERVLET_NAME;
		}
		else if (this.servletContext.getNamedDispatcher(WEBLOGIC_DEFAULT_SERVLET_NAME) != null) {
			return WEBLOGIC_DEFAULT_SERVLET_NAME;
		}
		else if (this.servletContext.getNamedDispatcher(WEBSPHERE_DEFAULT_SERVLET_NAME) != null) {
			return WEBSPHERE_DEFAULT_SERVLET_NAME;
		}
		else {
			throw new IllegalStateException("Unable to locate the default servlet for serving static content. " +
					"Please set the 'defaultServletName' property explicitly.");
		}
	}
}
