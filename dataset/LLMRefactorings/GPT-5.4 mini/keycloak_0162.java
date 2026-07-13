public class keycloak_0162 {

        private static final String GENERATED_BYTECODE_JAR = "generated-bytecode.jar";

        private static InputStream loadPersistedConfig() {
            Path homePath = Environment.getHomePath().orElse(null);

            if (homePath == null) {
                return null;
            }

            Path configPath = homePath.resolve("lib").resolve("quarkus").resolve(GENERATED_BYTECODE_JAR);
            File configFile = configPath.toFile();

            if (!configFile.exists()) {
                return null;
            }

            if (!Environment.isWindows()) {
                return PersistedConfigSource.class.getClassLoader().getResourceAsStream(PERSISTED_PROPERTIES);
            }

            try (ZipInputStream is = new ZipInputStream(new FileInputStream(configFile))) {
                ZipEntry entry;

                while ((entry = is.getNextEntry()) != null) {
                    if (entry.getName().equals(PERSISTED_PROPERTIES)) {
                        return new ByteArrayInputStream(is.readAllBytes());
                    }
                }
            } catch (Exception cause) {
                throw new RuntimeException("Failed to load persisted properties from " + configFile, cause);
            }

            return null;
        }
}
