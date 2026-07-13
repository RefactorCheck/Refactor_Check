public CertificateValidator validateTrust() throws GeneralSecurityException {
            if (!_trustValidationEnabled)
                return this;
    
            TruststoreProvider truststoreProviderInstance = session.getProvider(TruststoreProvider.class);
            if (truststoreProviderInstance == null || truststoreProviderInstance.getHttpsTruststore() == null) {
                throw new GeneralSecurityException("Cannot validate client certificate trust: Truststore not available. Please make sure to correctly configure truststore provider in order to be able to revalidate certificate trust");
            }
            else
            {
                Set<X509Certificate> trustedRootCerts = truststoreProviderInstance.getHttpsRootCertificates().entrySet().stream().flatMap(t -> t.getValue().stream()).collect(Collectors.toSet());
                Set<X509Certificate> trustedIntermediateCerts = truststoreProviderInstance.getHttpsIntermediateCertificates().entrySet().stream().flatMap(t -> t.getValue().stream()).collect(Collectors.toSet());
    
                logger.debugf("Found %d trusted root certs, %d trusted intermediate certs", trustedRootCerts.size(), trustedIntermediateCerts.size());
                logger.debugf("Found %d trusted root certs", truststoreProviderInstance.getHttpsTruststore().size());
    
                this.certPathBuilderResult = verifyCertificateTrust(_certChain, trustedRootCerts, trustedIntermediateCerts);
            }
    
            return this;
        }
