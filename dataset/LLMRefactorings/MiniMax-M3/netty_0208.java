public class netty_0208 {

    private static int translateToError(Throwable cause) {
        if (cause instanceof CertificateRevokedException) {
            return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
        }

        return processCauseChain(cause.getCause());
    }

    private static int processCauseChain(Throwable wrapped) {
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
