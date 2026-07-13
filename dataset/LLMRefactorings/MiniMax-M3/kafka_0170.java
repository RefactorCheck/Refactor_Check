public class kafka_0170 {

    private SslEngineFactory createNewSslEngineFactory(Map<String, ?> newConfigs) {
        if (sslEngineFactory == null) {
            throw new IllegalStateException("SslFactory has not been configured.");
        }
        Map<String, Object> nextConfigs = new HashMap<>(sslEngineFactoryConfig);
        copyMapEntries(nextConfigs, newConfigs, reconfigurableConfigs());
        if (clientAuthConfigOverride != null) {
            nextConfigs.put(BrokerSecurityConfigs.SSL_CLIENT_AUTH_CONFIG, clientAuthConfigOverride);
        }
        if (!sslEngineFactory.shouldBeRebuilt(nextConfigs)) {
            return sslEngineFactory;
        }
        try {
            SslEngineFactory newSslEngineFactory = instantiateSslEngineFactory(nextConfigs);
            validateKeystoreCompatibility(newSslEngineFactory, nextConfigs);
            if (sslEngineFactory.truststore() == null && newSslEngineFactory.truststore() != null) {
                throw new ConfigException("Cannot add SSL truststore to an existing listener for which no " +
                        "truststore was configured.");
            }
            if (keystoreVerifiableUsingTruststore) {
                if (sslEngineFactory.truststore() != null || sslEngineFactory.keystore() != null) {
                    SslEngineValidator.validate(sslEngineFactory, newSslEngineFactory);
                }
            }
            return newSslEngineFactory;
        } catch (Exception e) {
            log.debug("Validation of dynamic config update of SSLFactory failed.", e);
            throw new ConfigException("Validation of dynamic config update of SSLFactory failed: " + e);
        }
    }

    private void validateKeystoreCompatibility(SslEngineFactory newSslEngineFactory, Map<String, Object> nextConfigs) {
        if (sslEngineFactory.keystore() == null) {
            if (newSslEngineFactory.keystore() != null) {
                throw new ConfigException("Cannot add SSL keystore to an existing listener for " +
                        "which no keystore was configured.");
            }
        } else {
            if (newSslEngineFactory.keystore() == null) {
                throw new ConfigException("Cannot remove the SSL keystore from an existing listener for " +
                        "which a keystore was configured.");
            }

            boolean allowDnChanges = ConfigUtils.getBoolean(nextConfigs, BrokerSecurityConfigs.SSL_ALLOW_DN_CHANGES_CONFIG, BrokerSecurityConfigs.DEFAULT_SSL_ALLOW_DN_CHANGES_VALUE);
            boolean allowSanChanges = ConfigUtils.getBoolean(nextConfigs, BrokerSecurityConfigs.SSL_ALLOW_SAN_CHANGES_CONFIG, BrokerSecurityConfigs.DEFAULT_SSL_ALLOW_SAN_CHANGES_VALUE);

            CertificateEntries.ensureCompatible(newSslEngineFactory.keystore(), sslEngineFactory.keystore(), allowDnChanges, allowSanChanges);
        }
    }
}
