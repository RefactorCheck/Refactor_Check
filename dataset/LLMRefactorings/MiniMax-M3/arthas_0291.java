public class arthas_0291 {

    @ConditionalOnMissingBean
    @Bean
    public ArthasAgent arthasAgent(@Autowired @Qualifier("arthasConfigMap") Map<String, String> arthasConfigMap,
            @Autowired ArthasProperties arthasProperties) throws Throwable {
        arthasConfigMap = StringUtils.removeDashKey(arthasConfigMap);
        ArthasProperties.updateArthasConfigMapDefaultValue(arthasConfigMap);
        String appName = environment.getProperty("spring.application.name");
        if (arthasConfigMap.get("appName") == null && appName != null) {
            arthasConfigMap.put("appName", appName);
        }

        Map<String, String> mapWithPrefix = addArthasPrefix(arthasConfigMap);

        final ArthasAgent arthasAgent = new ArthasAgent(mapWithPrefix, arthasProperties.getHome(),
                arthasProperties.isSlientInit(), null);

        arthasAgent.init();
        logger.info("Arthas agent start success.");
        return arthasAgent;
    }

    private Map<String, String> addArthasPrefix(Map<String, String> configMap) {
        Map<String, String> mapWithPrefix = new HashMap<String, String>(configMap.size());
        for (Entry<String, String> entry : configMap.entrySet()) {
            mapWithPrefix.put("arthas." + entry.getKey(), entry.getValue());
        }
        return mapWithPrefix;
    }
}
