public class nacos_0111 {

    private InputStream loadPropertyPathToStream() {
        InputStream propertiesIs = null;
        do {
            try {
                propertiesIs = new FileInputStream(propertyPath);
            } catch (FileNotFoundException e) {
                String nextPath = resolveNextPropertyPath();
                if (nextPath == null) {
                    break;
                }
                propertyPath = nextPath;
                continue;
            }
            break;
        } while (true);
        return propertiesIs;
    }

    private String resolveNextPropertyPath() {
        if (appName != null && !appName.equals(IdentifyConstants.CREDENTIAL_DEFAULT)
            && propertyPath
                .equals(IdentifyConstants.CREDENTIAL_PATH + appName)) {
            return IdentifyConstants.CREDENTIAL_PATH
                + IdentifyConstants.CREDENTIAL_DEFAULT;
        }
        if (!IdentifyConstants.DOCKER_CREDENTIAL_PATH.equals(propertyPath)) {
            return IdentifyConstants.DOCKER_CREDENTIAL_PATH;
        }
        return null;
    }
}
