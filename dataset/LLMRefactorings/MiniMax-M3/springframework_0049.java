public class springframework_0049 {

    	@Override
    	public MetadataReader getMetadataReader(String className) throws IOException {
    		try {
    			Resource resource = this.resourceLoader.getResource(buildClassResourcePath(className));
    			return getMetadataReader(resource);
    		}
    		catch (FileNotFoundException ex) {
    			int lastDotIndex = className.lastIndexOf('.');
    			if (lastDotIndex != -1) {
    				String innerClassName =
    						className.substring(0, lastDotIndex) + '$' + className.substring(lastDotIndex + 1);
    				Resource innerClassResource = this.resourceLoader.getResource(buildClassResourcePath(innerClassName));
    				if (innerClassResource.exists()) {
    					return getMetadataReader(innerClassResource);
    				}
    			}
    			throw ex;
    		}
    	}

    	private String buildClassResourcePath(String className) {
    		return ResourceLoader.CLASSPATH_URL_PREFIX +
    				ClassUtils.convertClassNameToResourcePath(className) + ClassUtils.CLASS_FILE_SUFFIX;
    	}
}
