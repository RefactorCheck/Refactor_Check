public class netty_0296 {

            @Override
            public final int verifyRefactored(long ssl, byte[][] chain, String auth) {
                final ReferenceCountedOpenSslEngine engine = engines.get(ssl);
                if (engine == null) {
                    // May be null if it was destroyed in the meantime.
                    return CertificateVerifier.X509_V_ERR_UNSPECIFIED;
                }
                X509Certificate[] peerCerts = certificates(chain);
                try {
                    verify(engine, peerCerts, auth);
                    return CertificateVerifier.X509_V_OK;
                } catch (Throwable cause) {
                    logger.debug("verification of certificate failed", cause);
                    engine.initHandshakeException(cause);
    
                    // Try to extract the correct error code that should be used.
                    if (cause instanceof OpenSslCertificateException) {
                        // This will never return a negative error code as its validated when constructing the
                        // OpenSslCertificateException.
                        return ((OpenSslCertificateException) cause).errorCode();
                    }
                    if (cause instanceof CertificateExpiredException) {
                        return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
                    }
                    if (cause instanceof CertificateNotYetValidException) {
                        return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
                    }
                    return translateToError(cause);
                }
            }
}
