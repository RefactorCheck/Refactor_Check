public class netty_0208 {

            private static int translateToError(Throwable cause) {
                if (cause instanceof CertificateRevokedException) {
                    return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
                }
    
                // The X509TrustManagerImpl uses a Validator which wraps a CertPathValidatorException into
                // an CertificateException. So we need to handle the wrapped CertPathValidatorException to be
                // able to send the correct alert.
                Throwable wrapped = cause.getCause();
                while (wrapped != null) {
                    if (wrapped instanceof CertPathValidatorException) {
                        CertPathValidatorException ex = (CertPathValidatorException) wrapped;
                        CertPathValidatorException.Reason reason = ex.getReason();
                        if (reason == CertPathValidatorException.BasicReason.EXPIRED) {
                            return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
                        }
                        if (reason == CertPathValidatorException.BasicReason.NOT_YET_VALID) {
                            return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
                        }
                        if (reason == CertPathValidatorException.BasicReason.REVOKED) {
                            return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
                        }
                    }
                    wrapped = wrapped.getCause();
                }
                return CertificateVerifier.X509_V_ERR_UNSPECIFIED;
            }
}
