public class dubbo_0135 {

    private static final String FAILED_TO_OVERRIDE_FIELD_MESSAGE = "Failed to override field value of config bean: ";

    public void refresh() {
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
                        FAILED_TO_OVERRIDE_FIELD_MESSAGE + this,
                        e);
                throw new IllegalStateException(FAILED_TO_OVERRIDE_FIELD_MESSAGE + this, e);
            }

            postProcessRefresh();
        }
        refreshed.set(true);
    }
}
