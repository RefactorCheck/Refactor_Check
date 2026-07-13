public class keycloak_0079 {

        private String getEncryptedToken(TokenCategory category, String encodedToken) {
            String algAlgorithm = cekManagementAlgorithm(category);
            String encAlgorithm = encryptAlgorithm(category);
    
            JWEAlgorithmProvider jweAlgorithmProvider = getJweAlgorithmProvider(algAlgorithm);
            JWEEncryptionProvider jweEncryptionProvider = getJweEncryptionProvider(encAlgorithm);
    
            ClientModel client = session.getContext().getClient();
    
            KeyWrapper keyWrapper = PublicKeyStorageManager.getClientPublicKeyWrapper(session, client, JWK.Use.ENCRYPTION, algAlgorithm);
            if (keyWrapper == null) {
                throw new RuntimeException("can not get encryption KEK");
            }
            Key encryptionKek = keyWrapper.getPublicKey();
            String encryptionKekId = keyWrapper.getKid();
            try {
                return TokenUtil.jweKeyEncryptionEncode(encryptionKek, encodedToken.getBytes(StandardCharsets.UTF_8), algAlgorithm, encAlgorithm, encryptionKekId, jweAlgorithmProvider, jweEncryptionProvider);
            } catch (JWEException e) {
                throw new RuntimeException(e);
            }
        }
        
        private JWEAlgorithmProvider getJweAlgorithmProvider(String algAlgorithm) {
            CekManagementProvider cekManagementProvider = session.getProvider(CekManagementProvider.class, algAlgorithm);
            return cekManagementProvider.jweAlgorithmProvider();
        }
        
        private JWEEncryptionProvider getJweEncryptionProvider(String encAlgorithm) {
            ContentEncryptionProvider contentEncryptionProvider = session.getProvider(ContentEncryptionProvider.class, encAlgorithm);
            return contentEncryptionProvider.jweEncryptionProvider();
        }
}
