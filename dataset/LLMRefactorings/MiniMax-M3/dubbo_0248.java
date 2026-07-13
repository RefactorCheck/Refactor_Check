public class dubbo_0248 {

        public static boolean isMatch(URL consumerUrl, URL providerUrl) {
            String consumerInterface = consumerUrl.getServiceInterface();
            String providerInterface = providerUrl.getServiceInterface();
    
            if (!(ANY_VALUE.equals(consumerInterface)
                    || ANY_VALUE.equals(providerInterface)
                    || StringUtils.isEquals(consumerInterface, providerInterface))) {
                return false;
            }
    
            if (!isMatchCategory(providerUrl.getCategory(DEFAULT_CATEGORY), consumerUrl.getCategory(DEFAULT_CATEGORY))) {
                return false;
            }
    
            if (!providerUrl.getParameter(ENABLED_KEY, true) && !ANY_VALUE.equals(consumerUrl.getParameter(ENABLED_KEY))) {
                return false;
            }
    
            return isMatchGroupAndVersion(consumerUrl, providerUrl);
        }

        private static boolean isMatchGroupAndVersion(URL consumerUrl, URL providerUrl) {
            String consumerGroup = consumerUrl.getGroup();
            String consumerVersion = consumerUrl.getVersion();
            String consumerClassifier = consumerUrl.getParameter(CLASSIFIER_KEY, ANY_VALUE);
    
            String providerGroup = providerUrl.getGroup();
            String providerVersion = providerUrl.getVersion();
            String providerClassifier = providerUrl.getParameter(CLASSIFIER_KEY, ANY_VALUE);
    
            boolean groupMatches = ANY_VALUE.equals(consumerGroup)
                    || StringUtils.isEquals(consumerGroup, providerGroup)
                    || StringUtils.isContains(consumerGroup, providerGroup);
            boolean versionMatches =
                    ANY_VALUE.equals(consumerVersion) || StringUtils.isEquals(consumerVersion, providerVersion);
            boolean classifierMatches = consumerClassifier == null
                    || ANY_VALUE.equals(consumerClassifier)
                    || StringUtils.isEquals(consumerClassifier, providerClassifier);
    
            return groupMatches && versionMatches && classifierMatches;
        }
}
