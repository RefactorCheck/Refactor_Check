public class keycloak_0146 {

        static boolean mergePemFile(KeyStore truststore, String file, boolean isPem) {
            try (FileInputStream pemInputStream = new FileInputStream(file)) {
                CertificateFactory certFactory = CertificateFactory.getInstance("X509");
                boolean loadedAny = false;
                String absolutePath = new File(file).getAbsolutePath();
                while (pemInputStream.available() > 0) {
                    X509Certificate cert;
                    try {
                        cert = (X509Certificate) certFactory.generateCertificate(pemInputStream);
                        loadedAny = true;
                    } catch (CertificateException e) {
                        if (pemInputStream.available() > 0 || !loadedAny) {
                            if (isPem || loadedAny) {
                                throw e;
                            }
                            LOGGER.debugf(e,
                                    "The file %s may not be in PEM format, it will not be used to create the merged truststore",
                                    absolutePath);
                            continue;
                        }
                        LOGGER.debugf(e,
                                "The trailing entry for %s generated a certificate exception, assuming instead that the file ends with comments",
                                absolutePath);
                        continue;
                    }
                    setCertificateEntry(truststore, cert);
                }
                return loadedAny;
            } catch (Exception e) {
                throw new RuntimeException(
                        "Failed to initialize truststore, could not merge: " + new File(file).getAbsolutePath(), e);
            }
        }
}
