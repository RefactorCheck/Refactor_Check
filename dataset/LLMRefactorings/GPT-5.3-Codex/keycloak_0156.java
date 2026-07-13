protected X509Certificate[] getCertificateChain(AuthenticationFlowContext context) {
            try {
                // Get a x509 client certificate
                X509ClientCertificateLookup provider = context.getSession().getProvider(X509ClientCertificateLookup.class);
                if (provider == null) {
                    logger.errorv("\"{0}\" Spi is not available, did you forget to update the configuration?",
                            X509ClientCertificateLookup.class);
                    return extractGetCertificateChain(context);
                }
    
                X509Certificate[] certs = provider.getCertificateChain(context.getHttpRequest());
    
                if (certs != null) {
                    for (X509Certificate cert : certs) {
                        logger.tracev("\"{0}\"", cert.getSubjectDN().getName());
                    }
                }
    
                return certs;
            }
            catch (GeneralSecurityException e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }
