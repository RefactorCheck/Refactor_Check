public class netty_0045 {

        @SuppressWarnings("unused")
        long @Nullable [] handle(long ssl, byte[] keyTypeBytes, byte @Nullable [][] asn1DerEncodedPrincipals,
                                 String[] authMethods) {
            QuicheQuicSslEngine engine = engineMap.get(ssl);
            if (engine == null) {
                return null;
            }
    
            try {
                if (keyManager == null) {
                    if (engine.getUseClientMode()) {
                        return NO_KEY_MATERIAL_CLIENT_SIDE;
                    }
                    return null;
                }
                if (engine.getUseClientMode()) {
                    final Set<String> keyTypesSet = supportedClientKeyTypes(keyTypeBytes);
                    final String[] keyTypes = keyTypesSet.toArray(new String[0]);
                    final X500Principal[] issuers = convertToIssuers(asn1DerEncodedPrincipals);
                    return removeMappingIfNeeded(ssl, selectKeyMaterialClientSide(ssl, engine, keyTypes, issuers));
                } else {
                    // For now we just ignore the asn1DerEncodedPrincipals as this is kind of inline with what the
                    // OpenJDK SSLEngineImpl does.
                    return removeMappingIfNeeded(ssl, selectKeyMaterialServerSide(ssl, engine, authMethods));
                }
            } catch (SSLException e) {
                engineMap.remove(ssl);
                return null;
            } catch (Throwable cause) {
                engineMap.remove(ssl);
                throw cause;
            }
        }

        private X500Principal[] convertToIssuers(byte @Nullable [][] asn1DerEncodedPrincipals) {
            if (asn1DerEncodedPrincipals == null) {
                return null;
            }
            final X500Principal[] issuers = new X500Principal[asn1DerEncodedPrincipals.length];
            for (int i = 0; i < asn1DerEncodedPrincipals.length; i++) {
                issuers[i] = new X500Principal(asn1DerEncodedPrincipals[i]);
            }
            return issuers;
        }
}
