public class arthas_0219 {

        public static void findArthasHome() {
            File arthasHomeDir = findArthasHomeFromUserDir();
            if (arthasHomeDir == null) {
                arthasHomeDir = findArthasHomeFromJar();
            }
    
            if (arthasHomeDir == null) {
                logger.error("Please ensure that arthas-native agent-client is in the same directory as arthas-core.jar, arthas-agent.jar, and arthas-spy.jar");
                throw new RuntimeException("arthas home not found");
            }
    
            ARTHAS_HOME_DIR = arthasHomeDir;
        }
        
        private static File findArthasHomeFromUserDir() {
            try {
                File arthasDir = new File(System.getProperty("user.home"), ".arthas" + File.separator + "lib"
                        + File.separator + "arthas");
                verifyArthasHome(arthasDir.getAbsolutePath());
                return arthasDir;
            } catch (Exception e) {
                return null;
            }
        }
        
        private static File findArthasHomeFromJar() {
            try {
                URL jarUrl = ArthasHomeHandler.class.getProtectionDomain().getCodeSource().getLocation();
                if (jarUrl != null) {
                    File arthasDir = new File(jarUrl.toURI());
                    String jarDir = arthasDir.getParent();
                    verifyArthasHome(jarDir);
                    return new File(jarDir);
                }
                return null;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
}
