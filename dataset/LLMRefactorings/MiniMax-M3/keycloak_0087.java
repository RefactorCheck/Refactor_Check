public class keycloak_0087 {

        @Override
        protected VaultRawSecret obtainSecretInternal(String alias) {
            KeyStore ks;
            Key key;
            try {
                java.nio.file.Path realPath = keystorePath.toRealPath();
                char[] keystorePassChars = keystorePass.toCharArray();
                if (!Files.exists(realPath)) {
                    throw new VaultNotFoundException("The keystore file for Keycloak Vault was not found");
                }
                ks = KeyStore.getInstance(keystoreType);
                ks.load(Files.newInputStream(realPath), keystorePassChars);
                key = ks.getKey(alias, keystorePassChars);
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
