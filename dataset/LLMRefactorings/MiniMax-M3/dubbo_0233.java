public class dubbo_0233 {

        @Override
        public final void onMetadata(HEADER metadata) {
            httpMetadata = metadata;
            exceptionCustomizerWrapper.setMetadata(metadata);
    
            try {
                onBeforeMetadata(metadata);
            } catch (Throwable t) {
                logError(t);
                onMetadataError(metadata, t);
                return;
            }
    
            if (!initializeAndValidateExecutor(metadata)) {
                return;
            }
    
            executor.execute(() -> {
                try {
                    onPrepareMetadata(metadata);
                    setHttpMessageListener(buildHttpMessageListener());
                    onMetadataCompletion(metadata);
                } catch (Throwable t) {
                    logError(t);
                    onMetadataError(metadata, t);
                }
            });
        }
        
        private boolean initializeAndValidateExecutor(HEADER metadata) {
            try {
                executor = initializeExecutor(url, metadata);
            } catch (Throwable t) {
                LOGGER.error(LoggerCodeConstants.COMMON_ERROR_USE_THREAD_POOL, "Initialize executor failed.", t);
                onError(t);
                return false;
            }
            if (executor == null) {
                LOGGER.internalError("Executor must not be null.");
                onError(new NullPointerException("Initialize executor return null"));
                return false;
            }
            return true;
        }
}
