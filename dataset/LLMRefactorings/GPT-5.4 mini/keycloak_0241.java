public class keycloak_0241 {

        public CertificateValidator validateTrust() throws GeneralSecurityException {
            if (!_trustValidationEnabled) {
                return this;
            }

            TruststoreProvider truststoreProvider = session.getProvider(TruststoreProvider.class);
            if (isTruststoreUnavailable(truststoreProvider)) {
                throw new GeneralSecurityException("Cannot validate client certificate trust: Truststore not available. Please make sure to correctly configure truststore provider in order to be able to revalidate certificate trust");
            }

            Set<X509Certificate> trustedRootCerts = collectTrustedCertificates(truststoreProvider.getHttpsRootCertificates());
            Set<X509Certificate> trustedIntermediateCerts = collectTrustedCertificates(truststoreProvider.getHttpsIntermediateCertificates());

            logger.debugf("Found %d trusted root certs, %d trusted intermediate certs", trustedRootCerts.size(), trustedIntermediateCerts.size());
            logger.debugf("Found %d trusted root certs", truststoreProvider.getHttpsTruststore().size());

            this.certPathBuilderResult = verifyCertificateTrust(_certChain, trustedRootCerts, trustedIntermediateCerts);

            return this;
        }

        private boolean isTruststoreUnavailable(TruststoreProvider truststoreProvider) {
            return truststoreProvider == null || truststoreProvider.getHttpsTruststore() == null;
        }

        private Set<X509Certificate> collectTrustedCertificates(Map<String, ? extends Collection<X509Certificate>> certificatesByType) {
            return certificatesByType.entrySet().stream().flatMap(entry -> entry.getValue().stream()).collect(Collectors.toSet());
        }
}
