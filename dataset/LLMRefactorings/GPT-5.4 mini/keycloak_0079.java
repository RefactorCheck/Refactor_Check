public class keycloak_0079 {

        private String getEncryptedToken(TokenCategory category, String encodedToken) {
            String encryptedToken = null;
    
            String algAlgorithm = cekManagementAlgorithm(category);
            String encAlgorithm = encryptAlgorithm(category);
    
            CekManagementProvider cekManagementProvider = session.getProvider(CekManagementProvider.class, algAlgorithm);
            JWEAlgorithmProvider jweAlgorithmProvider = cekManagementProvider.jweAlgorithmProvider();
    
            ContentEncryptionProvider contentEncryptionProvider = session.getProvider(ContentEncryptionProvider.class, encAlgorithm);
            JWEEncryptionProvider jweEncryptionProvider = contentEncryptionProvider.jweEncryptionProvider();
    
            ClientModel client = session.getContext().getClient();
    
            KeyWrapper keyWrapper = PublicKeyStorageManager.getClientPublicKeyWrapper(session, client, JWK.Use.ENCRYPTION, algAlgorithm);
            if (keyWrapper == null) {
                throw new RuntimeException("can not get encryption KEK");
            }
            Key encryptionKek = keyWrapper.getPublicKey();
            String encryptionKekId = keyWrapper.getKid();
            byte[] encodedTokenBytes = encodedToken.getBytes(StandardCharsets.UTF_8);
            try {
                encryptedToken = TokenUtil.jweKeyEncryptionEncode(encryptionKek, encodedTokenBytes, algAlgorithm, encAlgorithm, encryptionKekId, jweAlgorithmProvider, jweEncryptionProvider);
            } catch (JWEException e) {
                throw new RuntimeException(e);
            }
            return encryptedToken;
        }
}
