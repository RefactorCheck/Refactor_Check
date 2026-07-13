public class keycloak_0176 {

            private X509CRL loadCRLFromFile(CertificateFactory cf, String relativePath) throws GeneralSecurityException {
                try {
                    String configDir = System.getProperty("jboss.server.config.dir");
                    if (configDir != null) {
                        File f = new File(configDir + File.separator + relativePath);
                        if (f.isFile()) {
                            String absolutePath = f.getAbsolutePath();
                            logger.debugf("Loading CRL from %s", absolutePath);

                            if (!f.canRead()) {
                                throw new IOException(String.format("Unable to read CRL from \"%s\"", absolutePath));
                            }
                            try (FileInputStream is = new FileInputStream(absolutePath)) {
                                return loadFromStream(cf, is);
                            }
                        }
                    }
                }
                catch(IOException ex) {
                    logger.errorf(ex.getMessage());
                }
                return null;
            }
}
