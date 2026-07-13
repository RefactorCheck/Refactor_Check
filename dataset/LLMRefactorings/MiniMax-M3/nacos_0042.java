public class nacos_0042 {

        private String findExecutableInPath(String command) {
            if (StringUtils.isBlank(getPathEnv())) {
                return null;
            }
            Set<String> directories = buildSearchDirectories();
            for (String each : directories) {
                Path candidate = Paths.get(expandHome(each), command).toAbsolutePath().normalize();
                if (Files.isRegularFile(candidate) && Files.isExecutable(candidate)) {
                    return candidate.toString();
                }
            }
            return null;
        }

        private Set<String> buildSearchDirectories() {
            String pathEnv = getPathEnv();
            Set<String> directories = new LinkedHashSet<>();
            for (String each : pathEnv.split(File.pathSeparator)) {
                if (StringUtils.isNotBlank(each)) {
                    directories.add(each.trim());
                }
            }
            String userHome = System.getProperty("user.home", "");
            if (StringUtils.isNotBlank(userHome)) {
                directories.add(Paths.get(userHome, ".local", "bin").toString());
            }
            return directories;
        }
}
