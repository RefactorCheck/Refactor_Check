public class nacos_0136 {

    public void configure(LoggerContext loggerContext, URI configLocation) throws IOException {
        Configuration nacosConfig = loadConfiguration(loggerContext, configLocation);
        
        // Key fix for issue #13940: Use initialize() instead of start()
        // initialize() sets up the configuration without triggering plugin reinitialization
        nacosConfig.initialize();
        
        // Get the current active configuration
        Configuration currentConfig = loggerContext.getConfiguration();
        
        mergeNacosAppenders(nacosConfig, currentConfig);
        mergeNacosLoggers(nacosConfig, currentConfig);
        
        // Apply the merged configuration
        loggerContext.updateLoggers();
        
        // Important: Do NOT call nacosConfig.stop() here!
        // The appenders and loggers have been transferred to currentConfig.
        // Calling stop() would shut down the appenders that are now owned by currentConfig.
        // nacosConfig will be garbage collected naturally, and since we only called initialize()
        // (not start()), there are no active background threads or resources to clean up.
    }

    // Additively merge Nacos appenders (non-invasive approach for middleware)
    // Note: Appenders are started individually and added to currentConfig
    // They are NOT removed from nacosConfig to avoid lifecycle issues
    private void mergeNacosAppenders(Configuration nacosConfig, Configuration currentConfig) {
        nacosConfig.getAppenders().values().forEach(appender -> {
            if (!appender.isStarted()) {
                appender.start();
            }
            currentConfig.addAppender(appender);
        });
    }

    // Add only Nacos-specific loggers to avoid interfering with user configuration
    private void mergeNacosLoggers(Configuration nacosConfig, Configuration currentConfig) {
        nacosConfig.getLoggers().entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(NACOS_LOGGER_PREFIX))
            .forEach(entry -> currentConfig.addLogger(entry.getKey(), entry.getValue()));
    }
}
