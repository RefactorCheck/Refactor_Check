public class kafka_0290 {

    private static final String TEMP_DIR_PREFIX = "test-plugins-resources.";

    private static Path extractResourceDirectoryFromJar(String resourceDir, URL resource) throws IOException {
        String prefix = resourceDir.endsWith("/") ? resourceDir : resourceDir + "/";
        Path tmpDir = TestUtils.tempDirectory(TEMP_DIR_PREFIX).toPath();
        JarURLConnection connection = (JarURLConnection) resource.openConnection();
        // Do not share the cached JarFile with the classloader, so we own it and can close it.
        connection.setUseCaches(false);
        try (JarFile jarFile = connection.getJarFile()) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (entry.isDirectory() || !name.startsWith(prefix)) {
                    continue;
                }
                Path dest = tmpDir.resolve(name.substring(prefix.length()));
                Files.createDirectories(dest.getParent());
                try (InputStream in = jarFile.getInputStream(entry)) {
                    Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
        return tmpDir;
    }
}
