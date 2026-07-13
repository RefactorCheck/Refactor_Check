public class springframework_0013 {

    	@Override
    	public Optional<Resource> apply(ServerRequest request) {
    		PathContainer pathContainer = request.requestPath().pathWithinApplication();
    		if (!this.pattern.matches(pathContainer)) {
    			return Optional.empty();
    		}
    
    		pathContainer = this.pattern.extractPathWithinPattern(pathContainer);
    		String path = ResourceHandlerUtils.normalizeInputPath(pathContainer.value());
    		if (ResourceHandlerUtils.shouldIgnoreInputPath(path)) {
    			return Optional.empty();
    		}
    
    		if (!(this.location instanceof UrlResource)) {
    			path = UriUtils.decode(path, StandardCharsets.UTF_8);
    		}
    
    		try {
    			Resource resource = this.location.createRelative(path);
    			if (resource.isReadable() && ResourceHandlerUtils.isResourceUnderLocation(this.location, resource)) {
    				return Optional.of(resource);
    			}
    			else {
    				return Optional.empty();
    			}
    		}
    		catch (IOException ex) {
    			throw new UncheckedIOException(ex);
    		}
    	}
}
