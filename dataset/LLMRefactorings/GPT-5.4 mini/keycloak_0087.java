public class keycloak_0087 {

        @Override
        protected VaultRawSecret obtainSecretInternal(String alias) {
            KeyStore ks;
            Key key;
            try {
                Path realKeystorePath = keystorePath.toRealPath();
                if (!Files.exists(realKeystorePath)) {
                    throw new VaultNotFoundException("The keystore file for Keycloak Vault was not found");
                }
                ks = KeyStore.getInstance(keystoreType);
                ks.load(Files.newInputStream(realKeystorePath), keystorePass.toCharArray());
                key = ks.getKey(alias, keystorePass.toCharArray());
                if (key == null) {
                    logger.warnf("Cannot find secret %s in %s", alias, keystorePath);
                    return DefaultVaultRawSecret.forBuffer(Optional.empty());
                }
            } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
                throw new RuntimeException(e);
            }
            return DefaultVaultRawSecret.forBuffer(Optional.of(ByteBuffer.wrap(new String(key.getEncoded()).getBytes())));
        }
}
