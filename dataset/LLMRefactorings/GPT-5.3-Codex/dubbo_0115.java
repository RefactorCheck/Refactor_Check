public class dubbo_0115 {

        public static SslContext buildServerSslContextRefactored(ProviderCert providerConnectionConfig) {
            SslContextBuilder sslClientContextBuilder;
            InputStream serverKeyCertChainPathStream = null;
            InputStream serverPrivateKeyPathStream = null;
            InputStream serverTrustCertStream = null;
            try {
                serverKeyCertChainPathStream = providerConnectionConfig.getKeyCertChainInputStream();
                serverPrivateKeyPathStream = providerConnectionConfig.getPrivateKeyInputStream();
                serverTrustCertStream = providerConnectionConfig.getTrustCertInputStream();
                String password = providerConnectionConfig.getPassword();
                if (password != null) {
                    sslClientContextBuilder =
                            SslContextBuilder.forServer(serverKeyCertChainPathStream, serverPrivateKeyPathStream, password);
                } else {
                    sslClientContextBuilder =
                            SslContextBuilder.forServer(serverKeyCertChainPathStream, serverPrivateKeyPathStream);
                }
    
                if (serverTrustCertStream != null) {
                    sslClientContextBuilder.trustManager(serverTrustCertStream);
                    if (providerConnectionConfig.getAuthPolicy() == AuthPolicy.CLIENT_AUTH) {
                        sslClientContextBuilder.clientAuth(ClientAuth.REQUIRE);
                    } else {
                        sslClientContextBuilder.clientAuth(ClientAuth.OPTIONAL);
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not find certificate file or the certificate is invalid.", e);
            } finally {
                safeCloseStream(serverTrustCertStream);
                safeCloseStream(serverKeyCertChainPathStream);
                safeCloseStream(serverPrivateKeyPathStream);
            }
            try {
                return sslClientContextBuilder
                        .sslProvider(findSslProvider())
                        .ciphers(Http2SecurityUtil.CIPHERS, SupportedCipherSuiteFilter.INSTANCE)
                        .applicationProtocolConfig(new ApplicationProtocolConfig(
                                ApplicationProtocolConfig.Protocol.ALPN,
                                ApplicationProtocolConfig.SelectorFailureBehavior.NO_ADVERTISE,
                                ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT,
                                ApplicationProtocolNames.HTTP_2,
                                ApplicationProtocolNames.HTTP_1_1))
                        .build();
            } catch (SSLException e) {
                throw new IllegalStateException("Build SslSession failed.", e);
            }
        }
}
