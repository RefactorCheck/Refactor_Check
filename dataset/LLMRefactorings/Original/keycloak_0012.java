public class keycloak_0012 {

        public String present(List<String> disclosureDigests,
                              boolean discloseAllClaims,
                              ObjectNode keyBindingClaims,
                              SignatureSignerContext holdSignatureSignerContext) {
            StringBuilder sb = new StringBuilder();
            if (discloseAllClaims) {
                // disclose everything
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
            String sd_hash = SdJwtUtils.hashAndBase64EncodeNoPad(unboundPresentation.getBytes(), getHashAlgorithm());
            keyBindingClaims.put(SD_HASH, sd_hash);
            KeyBindingJWT keyBindingJWT = KeyBindingJWT.builder()
                    .withPayload(keyBindingClaims)
                    .withSignerContext(holdSignatureSignerContext)
                    .build();
            sb.append(keyBindingJWT.getJws());
            return sb.toString();
        }
}
