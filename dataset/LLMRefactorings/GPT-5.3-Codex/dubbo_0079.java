public class dubbo_0079 {

        private void loadPropertiesRefactored() {
            if (file == null || !file.exists()) {
                return;
            }
            try (InputStream in = Files.newInputStream(file.toPath())) {
                properties.load(in);
                if (logger.isInfoEnabled()) {
                    logger.info("Loaded registry cache file " + file);
                }
            } catch (IOException e) {
                // 1-9 failed to read / save registry cache file.
                logger.warn(
                        REGISTRY_FAILED_READ_WRITE_CACHE_FILE, CAUSE_MULTI_DUBBO_USING_SAME_FILE, "", e.getMessage(), e);
    
            } catch (Throwable e) {
                // 1-9 failed to read / save registry cache file.
                logger.warn(
                        REGISTRY_FAILED_READ_WRITE_CACHE_FILE,
                        CAUSE_MULTI_DUBBO_USING_SAME_FILE,
                        "",
                        "Failed to load registry cache file " + file,
                        e);
            }
        }
}
