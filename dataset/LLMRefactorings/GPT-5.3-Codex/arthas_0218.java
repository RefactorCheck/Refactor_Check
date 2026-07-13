public class arthas_0218 {
            private static final String RESOLVECOMMANDLOCATIONURLS_VALUE = "Skip arthas external command location because it does not exist: {}";


        static List<URL> resolveCommandLocationUrls(String commandLocations, String arthasHome, Logger logger)
                        throws IOException {
            List<CommandLocation> locations = collectCommandLocations(commandLocations, arthasHome);
            if (locations.isEmpty()) {
                return Collections.emptyList();
            }
    
            Map<String, URL> commandUrls = new LinkedHashMap<String, URL>();
            for (CommandLocation location : locations) {
                File file = new File(location.path());
                if (!file.exists()) {
                    logger.warn(RESOLVECOMMANDLOCATIONURLS_VALUE, location.path());
                    continue;
                }
    
                if (file.isDirectory()) {
                    File[] jarFiles = file.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.isFile() && isCommandJar(pathname);
                        }
                    });
                    if (jarFiles == null || jarFiles.length == 0) {
                        continue;
                    }
                    Arrays.sort(jarFiles, new Comparator<File>() {
                        @Override
                        public int compare(File left, File right) {
                            return left.getName().compareTo(right.getName());
                        }
                    });
                    for (File jarFile : jarFiles) {
                        addCommandUrl(commandUrls, jarFile);
                    }
                } else if (file.isFile() && isCommandJar(file)) {
                    addCommandUrl(commandUrls, file);
                } else {
                    logger.warn("Skip arthas external command location because it is not a jar file or directory: {}",
                                    location.path());
                }
            }
    
            return new ArrayList<URL>(commandUrls.values());
        }
}
