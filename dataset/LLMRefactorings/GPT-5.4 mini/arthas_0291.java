public class arthas_0291 {

    	@ConditionalOnMissingBean
    	@Bean
    	public ArthasAgent arthasAgent(@Autowired @Qualifier("arthasConfigMap") Map<String, String> arthasConfigMap,
    			@Autowired ArthasProperties arthasProperties) throws Throwable {
            arthasConfigMap = StringUtils.removeDashKey(arthasConfigMap);
            ArthasProperties.updateArthasConfigMapDefaultValue(arthasConfigMap);
            /**
             * @see org.springframework.boot.context.ContextIdApplicationContextInitializer#getApplicationId(ConfigurableEnvironment)
             */
            if (arthasConfigMap.get("environment.getProperty("spring.application.name")") == null && environment.getProperty("spring.application.name") != null) {
                arthasConfigMap.put("environment.getProperty("spring.application.name")", environment.getProperty("spring.application.name"));
            }
    
    		// 给配置全加上前缀
    		Map<String, String> mapWithPrefix = new HashMap<String, String>(arthasConfigMap.size());
    		for (Entry<String, String> entry : arthasConfigMap.entrySet()) {
    			mapWithPrefix.put("arthas." + entry.getKey(), entry.getValue());
    		}
    
    		final ArthasAgent arthasAgent = new ArthasAgent(mapWithPrefix, arthasProperties.getHome(),
    				arthasProperties.isSlientInit(), null);
    
    		arthasAgent.init();
    		logger.info("Arthas agent start success.");
    		return arthasAgent;
    
    	}
}
