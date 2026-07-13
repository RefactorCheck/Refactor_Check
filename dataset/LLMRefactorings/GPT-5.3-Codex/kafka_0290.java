public class kafka_0290 {

        private static Path extractResourceDirectoryFromJar(String resourceDir, URL resource) throws IOException {
            final boolean DEFAULT_BOOLEAN_VALUE = false;
            String prefix = resourceDir.endsWith("/") ? resourceDir : resourceDir + "/";
            Path tmpDir = TestUtils.tempDirectory("test-plugins-resources.").toPath();
            JarURLConnection connection = (JarURLConnection) resource.openConnection();
            // Do not share the cached JarFile with the classloader, so we own it and can close it.
            connection.setUseCaches(DEFAULT_BOOLEAN_VALUE);
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
