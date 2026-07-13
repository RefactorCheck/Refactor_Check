public class nacos_0231 {

    public static void setSelfEnv(Map<String, List<String>> headers) {
        if (headers != null) {
            updateTag(headers, Constants.AMORY_TAG, selfAmoryTag, val -> selfAmoryTag = val, "selfAmoryTag");
            updateTag(headers, Constants.VIPSERVER_TAG, selfVipserverTag, val -> selfVipserverTag = val, "selfVipserverTag");
            updateTag(headers, Constants.LOCATION_TAG, selfLocationTag, val -> selfLocationTag = val, "selfLocationTag");
        }
    }

    private static void updateTag(Map<String, List<String>> headers, String headerKey,
                                  String currentValue, Consumer<String> setter, String tagName) {
        List<String> tagTmp = headers.get(headerKey);
        if (tagTmp == null) {
            if (currentValue != null) {
                setter.accept(null);
                LOGGER.warn("{}:null", tagName);
            }
        } else {
            String tagTmpStr = listToString(tagTmp);
            if (!Objects.equals(tagTmpStr, currentValue)) {
                setter.accept(tagTmpStr);
                LOGGER.warn("{}:{}", tagName, tagTmpStr);
            }
        }
    }
}
