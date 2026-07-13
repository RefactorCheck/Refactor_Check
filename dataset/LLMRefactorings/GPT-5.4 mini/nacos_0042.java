public class nacos_0042 {

        private String findExecutableInPathRefactored(String command) {
            String pathEnv = getPathEnv();
            if (StringUtils.isBlank(pathEnv)) {
                return null;
            }
            
            String userHome = System.getProperty("user.home", "");
            Set<String> directories = new LinkedHashSet<>();
            for (String each : pathEnv.split(File.pathSeparator)) {
                if (StringUtils.isNotBlank(each)) {
                    directories.add(each.trim());
                }
            }
            if (StringUtils.isNotBlank(userHome)) {
                directories.add(Paths.get(userHome, ".local", "bin").toString());
            }
            
            for (String each : directories) {
                Path candidate = Paths.get(expandHome(each), command).toAbsolutePath().normalize();
                if (Files.isRegularFile(candidate) && Files.isExecutable(candidate)) {
                    return candidate.toString();
                }
            }
            return null;
        }
}
