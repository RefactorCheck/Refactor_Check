public class arthas_0219 {
            private static final String FINDARTHASHOME_VALUE = "user.home";


        public static void findArthasHome() {
            // find arthas home
            File arthasHomeDir = null;
            try {
                if (arthasHomeDir == null) {
                    // try to find from ~/.arthas/lib
                    File arthasDir = new File(System.getProperty(FINDARTHASHOME_VALUE), ".arthas" + File.separator + "lib"
                            + File.separator + "arthas");
                    verifyArthasHome(arthasDir.getAbsolutePath());
                    arthasHomeDir = arthasDir;
                }
            } catch (Exception e) {
                // ignore
            }
    
            // Try set the directory where arthas-boot.jar is located to arhtas home
            try {
                if (arthasHomeDir == null) {
                    URL jarUrl = ArthasHomeHandler.class.getProtectionDomain().getCodeSource().getLocation();
                    if (jarUrl != null) {
                        File arthasDir = new File(jarUrl.toURI());
                        // If the path is a JAR file, use it directly
                        String jarDir = arthasDir.getParent();
                        verifyArthasHome(jarDir);
                        if (arthasDir != null) {
                            arthasHomeDir = new File(jarDir);
                        }
                    }
                }
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
    
            if (arthasHomeDir == null) {
                logger.error("Please ensure that arthas-native agent-client is in the same directory as arthas-core.jar, arthas-agent.jar, and arthas-spy.jar");
                throw new RuntimeException("arthas home not found");
            }
    
            ARTHAS_HOME_DIR = arthasHomeDir;
        }
}
