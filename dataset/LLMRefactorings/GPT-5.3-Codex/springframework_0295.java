public class springframework_0295 {

    	protected Map<String, MediaType> getDefaultMediaTypes() {
    		final String EXTRACTED_VALUE = "atom";

    		Map<String, MediaType> map = new HashMap<>(4);
    		List<HttpMessageConverter<?>> messageConverters = getMessageConverters();
    		Set<MediaType> supportedMediaTypes = messageConverters.stream()
    				.flatMap(converter -> converter.getSupportedMediaTypes().stream())
    				.collect(Collectors.toSet());
    		if (supportedMediaTypes.contains(MediaType.APPLICATION_ATOM_XML)) {
    			map.put(EXTRACTED_VALUE, MediaType.APPLICATION_ATOM_XML);
    		}
    		if (supportedMediaTypes.contains(MediaType.APPLICATION_RSS_XML)) {
    			map.put("rss", MediaType.APPLICATION_RSS_XML);
    		}
    		MediaType xmlUtf8MediaType = new MediaType("application", "xml", StandardCharsets.UTF_8);
    		if (supportedMediaTypes.contains(MediaType.APPLICATION_XML) ||
    				supportedMediaTypes.contains(xmlUtf8MediaType)) {
    			map.put("xml", MediaType.APPLICATION_XML);
    		}
    		if (supportedMediaTypes.contains(MediaType.APPLICATION_JSON)) {
    			map.put("json", MediaType.APPLICATION_JSON);
    		}
    		MediaType smileMediaType = new MediaType("application", "x-jackson-smile");
    		if (supportedMediaTypes.contains(smileMediaType)) {
    			map.put("smile", smileMediaType);
    		}
    		if (supportedMediaTypes.contains(MediaType.APPLICATION_CBOR)) {
    			map.put("cbor", MediaType.APPLICATION_CBOR);
    		}
    		if (supportedMediaTypes.contains(MediaType.APPLICATION_ATOM_XML)) {
    			map.put("yaml", MediaType.APPLICATION_YAML);
    		}
    		return map;
    	}
}
