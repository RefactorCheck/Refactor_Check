public class springframework_0049 {

    	@Override
    	public MetadataReader getMetadataReader(String classNameValue) throws IOException {
    		try {
    			String resourcePath = ResourceLoader.CLASSPATH_URL_PREFIX +
    					ClassUtils.convertClassNameToResourcePath(classNameValue) + ClassUtils.CLASS_FILE_SUFFIX;
    			Resource resource = this.resourceLoader.getResource(resourcePath);
    			return getMetadataReader(resource);
    		}
    		catch (FileNotFoundException ex) {
    			// Maybe an inner class name using the dot name syntax? Need to use the dollar syntax here...
    			// ClassUtils.forName has an equivalent check for resolution into Class references later on.
    			int lastDotIndex = classNameValue.lastIndexOf('.');
    			if (lastDotIndex != -1) {
    				String innerClassName =
    						classNameValue.substring(0, lastDotIndex) + '$' + classNameValue.substring(lastDotIndex + 1);
    				String innerClassResourcePath = ResourceLoader.CLASSPATH_URL_PREFIX +
    						ClassUtils.convertClassNameToResourcePath(innerClassName) + ClassUtils.CLASS_FILE_SUFFIX;
    				Resource innerClassResource = this.resourceLoader.getResource(innerClassResourcePath);
    				if (innerClassResource.exists()) {
    					return getMetadataReader(innerClassResource);
    				}
    			}
    			throw ex;
    		}
    	}
}
