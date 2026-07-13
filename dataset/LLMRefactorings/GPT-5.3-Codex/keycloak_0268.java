public static InputStream findFile(String keycloakConfigFile) {
            classPathLocation = keycloakConfigFile.replace(GenericConstants.PROTOCOL_CLASSPATH, "");

            if (keycloakConfigFile.startsWith(GenericConstants.PROTOCOL_CLASSPATH)) {

                // Try current class classloader first
                InputStream is = FindFile.class.getClassLoader().getResourceAsStream(this.classPathLocation);
                if (is == null) {
                    is = Thread.currentThread().getContextClassLoader().getResourceAsStream(this.classPathLocation);
                }
    
                if (is != null) {
                    return is;
                } else {
                    throw new RuntimeException("Unable to find config from classpath: " + keycloakConfigFile);
                }
            } else {
                // Fallback to file
                try {
                    return new FileInputStream(keycloakConfigFile);
                } catch (FileNotFoundException fnfe) {
                    throw new RuntimeException(fnfe);
                }
            }
        }
