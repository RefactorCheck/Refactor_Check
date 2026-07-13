public class springframework_0200 {

	@Override
	public @Nullable ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (HttpMethod.OPTIONS.matches(request.getMethod())) {
			response.setHeader(HttpHeaders.ALLOW, getAllowHeader());
			return null;
		}

		checkRequest(request);
		prepareResponse(response);

		if (this.synchronizeOnSession) {
			return doHandleRequestInSession(request, response);
		}

		return handleRequestInternal(request, response);
	}

	private @Nullable ModelAndView doHandleRequestInSession(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession(false);
		if (session != null) {
			Object mutex = WebUtils.getSessionMutex(session);
			synchronized (mutex) {
				return handleRequestInternal(request, response);
			}
		}
		return handleRequestInternal(request, response);
	}
}
