public class springframework_0295 {

    protected Map<String, MediaType> getDefaultMediaTypes() {
        Map<String, MediaType> map = new HashMap<>(4);
        List<HttpMessageConverter<?>> messageConverters = getMessageConverters();
        Set<MediaType> supportedMediaTypes = messageConverters.stream()
                .flatMap(converter -> converter.getSupportedMediaTypes().stream())
                .collect(Collectors.toSet());

        putIfSupported(map, supportedMediaTypes, MediaType.APPLICATION_ATOM_XML, "atom");
        putIfSupported(map, supportedMediaTypes, MediaType.APPLICATION_RSS_XML, "rss");

        MediaType xmlUtf8MediaType = new MediaType("application", "xml", StandardCharsets.UTF_8);
        if (supportedMediaTypes.contains(MediaType.APPLICATION_XML) ||
                supportedMediaTypes.contains(xmlUtf8MediaType)) {
            map.put("xml", MediaType.APPLICATION_XML);
        }

        putIfSupported(map, supportedMediaTypes, MediaType.APPLICATION_JSON, "json");

        MediaType smileMediaType = new MediaType("application", "x-jackson-smile");
        putIfSupported(map, supportedMediaTypes, smileMediaType, "smile");

        putIfSupported(map, supportedMediaTypes, MediaType.APPLICATION_CBOR, "cbor");

        if (supportedMediaTypes.contains(MediaType.APPLICATION_ATOM_XML)) {
            map.put("yaml", MediaType.APPLICATION_YAML);
        }

        return map;
    }

    private void putIfSupported(Map<String, MediaType> map, Set<MediaType> supportedMediaTypes,
            MediaType mediaType, String key) {
        if (supportedMediaTypes.contains(mediaType)) {
            map.put(key, mediaType);
        }
    }
}
