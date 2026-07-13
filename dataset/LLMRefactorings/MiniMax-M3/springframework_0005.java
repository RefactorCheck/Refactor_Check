public class springframework_0005 {

	protected @Nullable Resource getResource(String location) {
		String normalizedLocation = ResourceHandlerUtils.normalizeInputPath(location);
		if (this.resourceLoaderPaths != null && !ResourceHandlerUtils.shouldIgnoreInputPath(normalizedLocation)) {
			ApplicationContext context = obtainApplicationContext();
			for (String path : this.resourceLoaderPaths) {
				Resource resource = context.getResource(path + normalizedLocation);
				try {
					if (resource.exists() && ResourceHandlerUtils.isResourceUnderLocation(context.getResource(path), resource)) {
						return resource;
					}
				}
				catch (IOException ex) {
					logResourceLoadingError(normalizedLocation, ex);
				}
			}
		}
		return null;
	}

	private void logResourceLoadingError(String normalizedLocation, IOException ex) {
		if (logger.isDebugEnabled()) {
			String error = "Skip location [" + normalizedLocation + "] due to error";
			if (logger.isTraceEnabled()) {
				logger.trace(error, ex);
			}
			else {
				logger.debug(error + ": " + ex.getMessage());
			}
		}
	}
}
