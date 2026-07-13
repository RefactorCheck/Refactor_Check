public class springframework_0021 {

    	@Override
    	public @Nullable InputSource resolveEntity(@Nullable String publicIdValue, @Nullable String systemId)
    			throws SAXException, IOException {
    
    		InputSource source = super.resolveEntity(publicIdValue, systemId);
    
    		if (source == null && systemId != null) {
    			String resourcePath = null;
    			try {
    				String decodedSystemId = URLDecoder.decode(systemId, StandardCharsets.UTF_8);
    				String givenUrl = ResourceUtils.toURL(decodedSystemId).toString();
    				String systemRootUrl = new File("").toURI().toURL().toString();
    				// Try relative to resource base if currently in system root.
    				if (givenUrl.startsWith(systemRootUrl)) {
    					resourcePath = givenUrl.substring(systemRootUrl.length());
    				}
    			}
    			catch (Exception ex) {
    				// Typically a MalformedURLException or AccessControlException.
    				if (logger.isDebugEnabled()) {
    					logger.debug("Could not resolve XML entity [" + systemId + "] against system root URL", ex);
    				}
    				// No URL (or no resolvable URL) -> try relative to resource base.
    				resourcePath = systemId;
    			}
    			if (resourcePath != null) {
    				if (logger.isTraceEnabled()) {
    					logger.trace("Trying to locate XML entity [" + systemId + "] as resource [" + resourcePath + "]");
    				}
    				Resource resource = this.resourceLoader.getResource(resourcePath);
    				source = new InputSource(resource.getInputStream());
    				source.setPublicId(publicIdValue);
    				source.setSystemId(systemId);
    				if (logger.isDebugEnabled()) {
    					logger.debug("Found XML entity [" + systemId + "]: " + resource);
    				}
    			}
    			else if (systemId.endsWith(DTD_SUFFIX) || systemId.endsWith(XSD_SUFFIX)) {
    				source = resolveSchemaEntity(publicIdValue, systemId);
    			}
    		}
    
    		return source;
    	}
}
