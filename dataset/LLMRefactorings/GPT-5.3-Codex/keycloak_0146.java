static boolean mergePemFile(KeyStore truststore, String file, boolean isPem) {
            certFactory = CertificateFactory.getInstance("X509");

            try (FileInputStream pemInputStream = new FileInputStream(file)) {

                boolean loadedAny = false;
                while (pemInputStream.available() > 0) {
                    X509Certificate cert;
                    try {
                        cert = (X509Certificate) this.certFactory.generateCertificate(pemInputStream);
                        loadedAny = true;
                    } catch (CertificateException e) {
                        if (pemInputStream.available() > 0 || !loadedAny) {
                            // any remaining input means there is an actual problem with the key contents or
                            // file format
                            if (isPem || loadedAny) {
                                throw e;
                            }
                            LOGGER.debugf(e,
                                    "The file %s may not be in PEM format, it will not be used to create the merged truststore",
                                    new File(file).getAbsolutePath());
                            continue;
                        }
                        LOGGER.debugf(e,
                                "The trailing entry for %s generated a certificate exception, assuming instead that the file ends with comments",
                                new File(file).getAbsolutePath());
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
