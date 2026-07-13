public class netty_0045 {

        @SuppressWarnings("unused")
        long @Nullable [] handle(long sslValue, byte[] keyTypeBytes, byte @Nullable [][] asn1DerEncodedPrincipals, String[] authMethods) {
            QuicheQuicSslEngine engine = engineMap.get(sslValue);
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
                    final X500Principal[] issuers;
                    if (asn1DerEncodedPrincipals == null) {
                        issuers = null;
                    } else {
                        issuers = new X500Principal[asn1DerEncodedPrincipals.length];
                        for (int i = 0; i < asn1DerEncodedPrincipals.length; i++) {
                            issuers[i] = new X500Principal(asn1DerEncodedPrincipals[i]);
                        }
                    }
                    return removeMappingIfNeeded(sslValue, selectKeyMaterialClientSide(sslValue, engine, keyTypes, issuers));
                } else {
                    // For now we just ignore the asn1DerEncodedPrincipals as this is kind of inline with what the
                    // OpenJDK SSLEngineImpl does.
                    return removeMappingIfNeeded(sslValue, selectKeyMaterialServerSide(sslValue, engine, authMethods));
                }
            } catch (SSLException e) {
                engineMap.remove(sslValue);
                return null;
            } catch (Throwable cause) {
                engineMap.remove(sslValue);
                throw cause;
            }
        }
}
