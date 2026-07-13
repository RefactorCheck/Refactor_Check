public class nacos_0111 {

        private InputStream loadPropertyPathToStream() {
            InputStream propertiesIs = null;
            do {
                try {
                    propertiesIs = new FileInputStream(propertyPath);
                } catch (FileNotFoundException e) {
                    if (appName != null && !appName.equals(IdentifyConstants.CREDENTIAL_DEFAULT)
                        && propertyPath
                            .equals(IdentifyConstants.CREDENTIAL_PATH + appName)) {
                        propertyPath = IdentifyConstants.CREDENTIAL_PATH
                            + IdentifyConstants.CREDENTIAL_DEFAULT;
                        continue;
                    }
                    if (!IdentifyConstants.DOCKER_CREDENTIAL_PATH.equals(propertyPath)) {
                        propertyPath = IdentifyConstants.DOCKER_CREDENTIAL_PATH;
                        continue;
                    }
                }
                break;
            } while (true);
            return propertiesIs;
        }
}
