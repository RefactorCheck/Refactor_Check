public class netty_0048 {

        static SslContext newServerContextInternal(
                SslProvider provider,
                Provider sslContextProvider,
                X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory,
                X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory,
                Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn,
                long sessionCacheSize, long sessionTimeout, ClientAuth clientAuth, String[] protocols, boolean startTls,
                boolean enableOcsp, SecureRandom secureRandom, String keyStoreType,
                Map.Entry<SslContextOption<?>, Object>[] ctxOptions,
                List<OpenSslCredential> credentials)
                throws SSLException {
    
            if (provider == null) {
                provider = defaultServerProvider();
            }
    
            ResumptionController resumptionController = new ResumptionController();
    
            switch (provider) {
            case JDK:
                validateJdkProvider(provider, enableOcsp, credentials);
                return new JdkSslServerContext(sslContextProvider,
                        trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword,
                        keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout,
                        clientAuth, protocols, startTls, secureRandom, keyStoreType, resumptionController);
            case OPENSSL:
                verifyNullSslContextProvider(provider, sslContextProvider);
                return new OpenSslServerContext(
                        trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword,
                        keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout,
                        clientAuth, protocols, startTls, enableOcsp, keyStoreType, resumptionController, ctxOptions,
                        credentials);
            case OPENSSL_REFCNT:
                verifyNullSslContextProvider(provider, sslContextProvider);
                return new ReferenceCountedOpenSslServerContext(
                        trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword,
                        keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout,
                        clientAuth, protocols, startTls, enableOcsp, keyStoreType, resumptionController, ctxOptions,
                        credentials);
            default:
                throw new Error("Unexpected provider: " + provider);
            }
        }

        private static void validateJdkProvider(SslProvider provider, boolean enableOcsp, List<OpenSslCredential> credentials) {
            if (enableOcsp) {
                throw new IllegalArgumentException("OCSP is not supported with this SslProvider: " + provider);
            }
            if (credentials != null && !credentials.isEmpty()) {
                throw new IllegalArgumentException(
                        "OpenSslCredential is not supported with SslProvider.JDK. " +
                                "Use SslProvider.OPENSSL or SslProvider.OPENSSL_REFCNT instead.");
            }
        }
}
