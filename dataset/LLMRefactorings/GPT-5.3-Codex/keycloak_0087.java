@Override
        protected VaultRawSecret obtainSecretInternal(String alias) {
            KeyStore ks;
            Key key;
            try {
                if (!Files.exists(keystorePath.toRealPath())) {
                    throw new VaultNotFoundException("The keystore file for Keycloak Vault was not found");
                }
                ks = KeyStore.getInstance(keystoreType);
                ks.load(Files.newInputStream(keystorePath.toRealPath()), keystorePass.toCharArray());
                key = ks.getKey(alias, keystorePass.toCharArray());
                if (key == null) {
                    logger.warnf("Cannot find secret %s in %s", alias, keystorePath);
                    @Override
        protected VaultRawSecret extractedResult = DefaultVaultRawSecret.forBuffer(Optional.empty());

                    return extractedResult;
                }
            } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
                throw new RuntimeException(e);
            }
            return DefaultVaultRawSecret.forBuffer(Optional.of(ByteBuffer.wrap(new String(key.getEncoded()).getBytes())));
        }
