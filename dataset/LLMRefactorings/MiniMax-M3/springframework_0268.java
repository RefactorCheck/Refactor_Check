public class springframework_0268 {

	@Override
	protected @Nullable ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ServletContext servletContext = getServletContext();
		Assert.state(servletContext != null, "No ServletContext");
		RequestDispatcher rd = servletContext.getNamedDispatcher(this.servletName);
		if (rd == null) {
			throw new ServletException("No servlet with name '" + this.servletName + "' defined in web.xml");
		}

		if (useInclude(request, response)) {
			rd.include(request, response);
			logTrace("Included");
		}
		else {
			rd.forward(request, response);
			logTrace("Forwarded to");
		}

		return null;
	}

	private void logTrace(String action) {
		if (logger.isTraceEnabled()) {
			logger.trace(action + " servlet [" + this.servletName +
					"] in ServletForwardingController '" + this.beanName + "'");
		}
	}
}
