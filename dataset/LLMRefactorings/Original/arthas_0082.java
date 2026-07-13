public class arthas_0082 {

        public static String findJavaHome() {
            if (FOUND_JAVA_HOME != null) {
                return FOUND_JAVA_HOME;
            }
    
            String javaHome = System.getProperty("java.home");
    
            if (JavaVersionUtils.isLessThanJava9()) {
                File toolsJar = new File(javaHome, "lib/tools.jar");
                if (!toolsJar.exists()) {
                    toolsJar = new File(javaHome, "../lib/tools.jar");
                }
                if (!toolsJar.exists()) {
                    // maybe jre
                    toolsJar = new File(javaHome, "../../lib/tools.jar");
                }
    
                if (toolsJar.exists()) {
                    FOUND_JAVA_HOME = javaHome;
                    return FOUND_JAVA_HOME;
                }
    
                if (!toolsJar.exists()) {
                    AnsiLog.debug("Can not find tools.jar under java.home: " + javaHome);
                    String javaHomeEnv = System.getenv("JAVA_HOME");
                    if (javaHomeEnv != null && !javaHomeEnv.isEmpty()) {
                        AnsiLog.debug("Try to find tools.jar in System Env JAVA_HOME: " + javaHomeEnv);
                        // $JAVA_HOME/lib/tools.jar
                        toolsJar = new File(javaHomeEnv, "lib/tools.jar");
                        if (!toolsJar.exists()) {
                            // maybe jre
                            toolsJar = new File(javaHomeEnv, "../lib/tools.jar");
                        }
                    }
    
                    if (toolsJar.exists()) {
                        AnsiLog.info("Found java home from System Env JAVA_HOME: " + javaHomeEnv);
                        FOUND_JAVA_HOME = javaHomeEnv;
                        return FOUND_JAVA_HOME;
                    }
    
                    throw new IllegalArgumentException("Can not find tools.jar under java home: " + javaHome
                            + ", please try to start arthas-boot with full path java. Such as /opt/jdk/bin/java -jar arthas-boot.jar");
                }
            } else {
                FOUND_JAVA_HOME = javaHome;
            }
            return FOUND_JAVA_HOME;
        }
}
