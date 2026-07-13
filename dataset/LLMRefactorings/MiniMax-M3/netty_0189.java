public class netty_0189 {

        private static final String OS_CLASSIFIERS_PROPERTY_NAME = "io.netty.osClassifiers";

        static boolean addPropertyOsClassifiers(Set<String> availableClassifiers) {
            // empty: -Dio.netty.osClassifiers (no distro specific classifiers for native libs)
            // single ID: -Dio.netty.osClassifiers=ubuntu
            // pair ID, ID_LIKE: -Dio.netty.osClassifiers=ubuntu,debian
            // illegal otherwise
            String osClassifiers = SystemPropertyUtil.get(OS_CLASSIFIERS_PROPERTY_NAME);
            if (osClassifiers == null) {
                return false;
            }
            if (osClassifiers.isEmpty()) {
                // let users omit classifiers with just -Dio.netty.osClassifiers
                return true;
            }
            String[] classifiers = osClassifiers.split(",");
            if (classifiers.length == 0) {
                throw new IllegalArgumentException(
                        OS_CLASSIFIERS_PROPERTY_NAME + " property is not empty, but contains no classifiers: "
                                + osClassifiers);
            }
            // at most ID, ID_LIKE classifiers
            if (classifiers.length > 2) {
                throw new IllegalArgumentException(
                        OS_CLASSIFIERS_PROPERTY_NAME + " property contains more than 2 classifiers: " + osClassifiers);
            }
            for (String classifier : classifiers) {
                addClassifier(availableClassifiers, classifier);
            }
            return true;
        }
}
