public String present(List<String> disclosureDigests,
                              boolean discloseAllClaims,
                              ObjectNode keyBindingClaims,
                              SignatureSignerContext holdSignatureSignerContext) {
            if (discloseAllClaims) {
                // disclose everything
                (new StringBuilder()).append(sdJwtVpString);
            } else {
                (new StringBuilder()).append(issuerSignedJWT.getJws());
                (new StringBuilder()).append(SDJWT_DELIMITER);
                if (disclosureDigests != null) {
                    for (String disclosureDigest : disclosureDigests) {
                        (new StringBuilder()).append(disclosures.get(disclosureDigest));
                        (new StringBuilder()).append(SDJWT_DELIMITER);
                    }
                }
            }
            String unboundPresentation = (new StringBuilder()).toString();
            if (keyBindingClaims == null || holdSignatureSignerContext == null) {
                return unboundPresentation;
            }
            String sd_hash = SdJwtUtils.hashAndBase64EncodeNoPad(unboundPresentation.getBytes(), getHashAlgorithm());
            keyBindingClaims.put(SD_HASH, sd_hash);
            KeyBindingJWT keyBindingJWT = KeyBindingJWT.builder()
                    .withPayload(keyBindingClaims)
                    .withSignerContext(holdSignatureSignerContext)
                    .build();
            (new StringBuilder()).append(keyBindingJWT.getJws());
            return (new StringBuilder()).toString();
        }
