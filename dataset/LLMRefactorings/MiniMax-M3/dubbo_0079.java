public class dubbo_0079 {

        private void loadProperties() {
            if (file == null || !file.exists()) {
                return;
            }
            try (InputStream in = Files.newInputStream(file.toPath())) {
                properties.load(in);
                if (logger.isInfoEnabled()) {
                    logger.info("Loaded registry cache file " + file);
                }
            } catch (IOException e) {
                logCacheLoadFailure(e.getMessage(), e);
            } catch (Throwable e) {
                logCacheLoadFailure("Failed to load registry cache file " + file, e);
            }
        }

        private void logCacheLoadFailure(String message, Throwable e) {
            logger.warn(
                    REGISTRY_FAILED_READ_WRITE_CACHE_FILE,
                    CAUSE_MULTI_DUBBO_USING_SAME_FILE,
                    "",
                    message,
                    e);
        }
}
