public class keycloak_0286 {

            @Override
            public OCSPProvider.OCSPRevocationStatus check(X509Certificate cert, X509Certificate issuerCertificate) throws CertPathValidatorException {
    
                OCSPProvider.OCSPRevocationStatus ocspRevocationStatus = null;
                if (responderUri == null || responderUri.trim().length() == 0) {
                    // Obtains revocation status of a certificate using OCSP and assuming
                    // most common defaults. If responderUri is not specified,
                    // then OCS responder URI is retrieved from the
                    // certificate's AIA extension.
                    // OCSP responses must be signed with the issuer certificate
                    // or with another certificate that must be:
                    // 1) signed by the issuer certificate,
                    // 2) Includes the value of OCSPsigning in ExtendedKeyUsage v3 extension
                    // 3) Certificate is valid at the time
                    OCSPProvider ocspProvider = CryptoIntegration.getProvider().getOCSPProver(OCSPProvider.class);
                    ocspRevocationStatus = ocspProvider.check(session, cert, issuerCertificate);
                }
                else {
                    URI uri;
                    try {
                        uri = new URI(responderUri);
                    } catch (URISyntaxException e) {
                        String message = String.format("Unable to check certificate revocation status using OCSP.\n%s", e.getMessage());
                        throw new CertPathValidatorException(message, e);
                    }
                    logger.tracef("Responder URI \"%s\" will be used to verify revocation status of the certificate using OCSP with responderCert=%s",
                            uri.toString(), responderCert);
                    // Obtains the revocation status of a certificate using OCSP.
                    // OCSP responder's certificate is assumed to be the issuer's certificate
                    // certificate.
                    // responderUri overrides the contents (if any) of the certificate's AIA extension
                    OCSPProvider ocspProvider = CryptoIntegration.getProvider().getOCSPProver(OCSPProvider.class);
                    ocspRevocationStatus = ocspProvider.check(session, cert, issuerCertificate, uri, responderCert, null);
                }
                return ocspRevocationStatus;
            }
}
