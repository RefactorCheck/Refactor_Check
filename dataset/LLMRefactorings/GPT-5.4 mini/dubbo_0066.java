public static class dubbo_0066 {

        protected <V> V delay(String configFilePath, ThrowableFunction<File, V> function) {
            File configFile = new File(configFilePath);
            // Must be based on PoolingWatchService and has listeners under config file
            if (isBasedPoolingWatchService()) {
                File configDirectory = configFile.getParentFile();
                executeMutually(configDirectory, () -> {
                    if (hasListeners(configFile) && isProcessing(configDirectory)) {
                        Integer delay = getDelay();
                        if (delay != null) {
                            // wait for delay in seconds
                            long timeout = SECONDS.toMillis(delay);
                            if (logger.isDebugEnabled()) {
                                logger.debug(format(
                                        "The config[path : %s] is about to delay in %d ms.", configFilePath, timeout));
                            }
                            configDirectory.wait(timeout);
                        }
                    }
                    addProcessing(configDirectory);
                    return null;
                });
            }
    
            V value = null;
    
            try {
                value = function.apply(configFile);
            } catch (Throwable e) {
                if (logger.isErrorEnabled()) {
                    logger.error(CONFIG_ERROR_PROCESS_LISTENER, "", "", e.getMessage(), e);
                }
            }
    
            return value;
        }
}
