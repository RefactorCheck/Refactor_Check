public class keycloak_0012 {

    public String present(List<String> disclosureDigests,
                          boolean discloseAllClaims,
                          ObjectNode keyBindingClaims,
                          SignatureSignerContext holdSignatureSignerContext) {
        StringBuilder sb = new StringBuilder();
        if (discloseAllClaims) {
            sb.append(sdJwtVpString);
        } else {
            sb.append(issuerSignedJWT.getJws());
            sb.append(SDJWT_DELIMITER);
            if (disclosureDigests != null) {
                for (String disclosureDigest : disclosureDigests) {
                    sb.append(disclosures.get(disclosureDigest));
                    sb.append(SDJWT_DELIMITER);
                }
            }
        }
        String unboundPresentation = sb.toString();
        if (keyBindingClaims == null || holdSignatureSignerContext == null) {
            return unboundPresentation;
        }
        sb.append(createKeyBinding(keyBindingClaims, holdSignatureSignerContext, unboundPresentation).getJws());
        return sb.toString();
    }

    private KeyBindingJWT createKeyBinding(ObjectNode keyBindingClaims,
                                           SignatureSignerContext holdSignatureSignerContext,
                                           String unboundPresentation) {
        String sd_hash = SdJwtUtils.hashAndBase64EncodeNoPad(unboundPresentation.getBytes(), getHashAlgorithm());
        keyBindingClaims.put(SD_HASH, sd_hash);
        return KeyBindingJWT.builder()
                .withPayload(keyBindingClaims)
                .withSignerContext(holdSignatureSignerContext)
                .build();
    }
}
