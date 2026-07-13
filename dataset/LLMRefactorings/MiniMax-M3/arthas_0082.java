public class arthas_0082 {

        public static String findJavaHome() {
            if (FOUND_JAVA_HOME != null) {
                return FOUND_JAVA_HOME;
            }
    
            String javaHome = System.getProperty("java.home");
    
            if (JavaVersionUtils.isLessThanJava9()) {
                if (findToolsJar(javaHome) != null) {
                    FOUND_JAVA_HOME = javaHome;
                    return FOUND_JAVA_HOME;
                }
    
                AnsiLog.debug("Can not find tools.jar under java.home: " + javaHome);
                String javaHomeEnv = System.getenv("JAVA_HOME");
                if (javaHomeEnv != null && !javaHomeEnv.isEmpty()) {
                    AnsiLog.debug("Try to find tools.jar in System Env JAVA_HOME: " + javaHomeEnv);
                    if (findToolsJar(javaHomeEnv) != null) {
                        AnsiLog.info("Found java home from System Env JAVA_HOME: " + javaHomeEnv);
                        FOUND_JAVA_HOME = javaHomeEnv;
                        return FOUND_JAVA_HOME;
                    }
                }
    
                throw new IllegalArgumentException("Can not find tools.jar under java home: " + javaHome
                        + ", please try to start arthas-boot with full path java. Such as /opt/jdk/bin/java -jar arthas-boot.jar");
            } else {
                FOUND_JAVA_HOME = javaHome;
            }
            return FOUND_JAVA_HOME;
        }

        private static File findToolsJar(String javaHome) {
            File toolsJar = new File(javaHome, "lib/tools.jar");
            if (!toolsJar.exists()) {
                toolsJar = new File(javaHome, "../lib/tools.jar");
            }
            if (!toolsJar.exists()) {
                toolsJar = new File(javaHome, "../../lib/tools.jar");
            }
            return toolsJar.exists() ? toolsJar : null;
        }
}
