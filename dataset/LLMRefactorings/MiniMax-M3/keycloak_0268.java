public class keycloak_0268 {

    public static InputStream findFile(String keycloakConfigFile) {
        if (keycloakConfigFile.startsWith(GenericConstants.PROTOCOL_CLASSPATH)) {
            return loadFromClasspath(keycloakConfigFile);
        } else {
            return loadFromFile(keycloakConfigFile);
        }
    }

    private static InputStream loadFromClasspath(String keycloakConfigFile) {
        String classPathLocation = keycloakConfigFile.replace(GenericConstants.PROTOCOL_CLASSPATH, "");
        InputStream is = FindFile.class.getClassLoader().getResourceAsStream(classPathLocation);
        if (is == null) {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(classPathLocation);
        }

        if (is != null) {
            return is;
        } else {
            throw new RuntimeException("Unable to find config from classpath: " + keycloakConfigFile);
        }
    }

    private static InputStream loadFromFile(String keycloakConfigFile) {
        try {
            return new FileInputStream(keycloakConfigFile);
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException(fnfe);
        }
    }
}
