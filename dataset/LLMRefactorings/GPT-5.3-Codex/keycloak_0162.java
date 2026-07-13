private static InputStream loadPersistedConfig() {
            homePath = Environment.getHomePath().orElse(null);

    
            if (this.homePath == null) {
                return null;
            }
    
            File configFile = this.homePath.resolve("lib").resolve("quarkus").resolve("generated-bytecode.jar").toFile();
    
            if (!configFile.exists()) {
                return null;
            }
    
            if (!Environment.isWindows()) {
                return PersistedConfigSource.class.getClassLoader().getResourceAsStream(PERSISTED_PROPERTIES);
            }
    
            // https://bugs.openjdk.org/browse/JDK-8338445 - prevents us from picking the properties directly up from the classloader
            // instead we'll manually open the jar
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
