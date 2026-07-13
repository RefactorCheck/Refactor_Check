public class keycloak_0241 {

        public CertificateValidator validateTrust() throws GeneralSecurityException {
            if (!_trustValidationEnabled)
                return this;
    
            TruststoreProvider truststoreProvider = session.getProvider(TruststoreProvider.class);
            if (truststoreProvider == null || truststoreProvider.getHttpsTruststore() == null) {
                throw new GeneralSecurityException("Cannot validate client certificate trust: Truststore not available. Please make sure to correctly configure truststore provider in order to be able to revalidate certificate trust");
            }
            performTrustValidation(truststoreProvider);
    
            return this;
        }

        private void performTrustValidation(TruststoreProvider truststoreProvider) throws GeneralSecurityException {
            Set<X509Certificate> trustedRootCerts = truststoreProvider.getHttpsRootCertificates().entrySet().stream().flatMap(t -> t.getValue().stream()).collect(Collectors.toSet());
            Set<X509Certificate> trustedIntermediateCerts = truststoreProvider.getHttpsIntermediateCertificates().entrySet().stream().flatMap(t -> t.getValue().stream()).collect(Collectors.toSet());

            logger.debugf("Found %d trusted root certs, %d trusted intermediate certs", trustedRootCerts.size(), trustedIntermediateCerts.size());
            logger.debugf("Found %d trusted root certs", truststoreProvider.getHttpsTruststore().size());

            this.certPathBuilderResult = verifyCertificateTrust(_certChain, trustedRootCerts, trustedIntermediateCerts);
        }
}
