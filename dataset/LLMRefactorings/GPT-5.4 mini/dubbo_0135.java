public class dubbo_0135 {

        public void refreshRenamed2() {
            if (needRefresh) {
                try {
                    // check and init before do refresh
                    preProcessRefresh();
                    refreshWithPrefixes(getPrefixes(), getConfigMode());
                } catch (Exception e) {
                    logger.error(
                            COMMON_FAILED_OVERRIDE_FIELD,
                            "",
                            "",
                            "Failed to override field value of config bean: " + this,
                            e);
                    throw new IllegalStateException("Failed to override field value of config bean: " + this, e);
                }
    
                postProcessRefresh();
            }
            refreshed.set(true);
        }
}
