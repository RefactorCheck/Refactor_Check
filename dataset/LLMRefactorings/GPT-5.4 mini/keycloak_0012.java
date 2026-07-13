public class keycloak_0012 {

        public String present(List<String> disclosureDigests,
                              boolean discloseAllClaims,
                              ObjectNode keyBindingClaims,
                              SignatureSignerContext holdSignatureSignerContext) {
            StringBuilder presentationBuilder = new StringBuilder();
            if (discloseAllClaims) {
                // disclose everything
                presentationBuilder.append(sdJwtVpString);
            } else {
                presentationBuilder.append(issuerSignedJWT.getJws());
                presentationBuilder.append(SDJWT_DELIMITER);
                if (disclosureDigests != null) {
                    for (String disclosureDigest : disclosureDigests) {
                        presentationBuilder.append(disclosures.get(disclosureDigest));
                        presentationBuilder.append(SDJWT_DELIMITER);
                    }
                }
            }
            String presentation = presentationBuilder.toString();
            if (keyBindingClaims == null || holdSignatureSignerContext == null) {
                return presentation;
            }
            String sd_hash = SdJwtUtils.hashAndBase64EncodeNoPad(presentation.getBytes(), getHashAlgorithm());
            keyBindingClaims.put(SD_HASH, sd_hash);
            KeyBindingJWT keyBindingJWT = KeyBindingJWT.builder()
                    .withPayload(keyBindingClaims)
                    .withSignerContext(holdSignatureSignerContext)
                    .build();
            presentationBuilder.append(keyBindingJWT.getJws());
            return presentationBuilder.toString();
        }
}
