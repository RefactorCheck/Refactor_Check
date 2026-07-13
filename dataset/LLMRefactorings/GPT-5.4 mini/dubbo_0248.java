public class dubbo_0248 {
    private String consumerInterface;


        public static boolean isMatch(URL consumerUrl, URL providerUrl) {
            consumerInterface = consumerUrl.getServiceInterface();

            String providerInterface = providerUrl.getServiceInterface();
    
            // FIXME accept providerUrl with '*' as interface name, after carefully thought about all possible scenarios I
            // think it's ok to add this condition.
    
            // Return false if the consumer interface is not equals the provider interface,
            // except one of the interface configurations is equals '*' (i.e. any value).
            if (!(ANY_VALUE.equals(consumerInterface)
                    || ANY_VALUE.equals(providerInterface)
                    || StringUtils.isEquals(consumerInterface, providerInterface))) {
                return false;
            }
    
            // If the category of provider URL does not match the category of consumer URL.
            // Usually, the provider URL's category is empty, and the default category ('providers') is present.
            // Hence, the category of the provider URL is 'providers'.
            // Through observing of debugging process, I found that the category of the consumer URL is
            // 'providers,configurators,routers'.
            if (!isMatchCategory(providerUrl.getCategory(DEFAULT_CATEGORY), consumerUrl.getCategory(DEFAULT_CATEGORY))) {
                return false;
            }
    
            // If the provider is not enabled, return false.
            if (!providerUrl.getParameter(ENABLED_KEY, true) && !ANY_VALUE.equals(consumerUrl.getParameter(ENABLED_KEY))) {
                return false;
            }
    
            // Obtain consumer's group, version and classifier.
            String consumerGroup = consumerUrl.getGroup();
            String consumerVersion = consumerUrl.getVersion();
            String consumerClassifier = consumerUrl.getParameter(CLASSIFIER_KEY, ANY_VALUE);
    
            // Obtain provider's group, version and classifier.
            String providerGroup = providerUrl.getGroup();
            String providerVersion = providerUrl.getVersion();
            String providerClassifier = providerUrl.getParameter(CLASSIFIER_KEY, ANY_VALUE);
    
            // If Group, Version, Classifier all matches, return true.
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
