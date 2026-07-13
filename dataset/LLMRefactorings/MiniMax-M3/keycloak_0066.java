public class keycloak_0066 {

        public static IdentityBrokerState decoded(String state, String clientId, String clientClientId, String tabId, String clientData) {
    
            String clientIdEncoded = clientClientId;
            boolean isUuid = false;
            if (clientId != null) {
                String uuidEncoded = tryEncodeUuid(clientId);
                if (uuidEncoded != null) {
                    clientIdEncoded = uuidEncoded;
                    isUuid = true;
                }
            }
            if (!isUuid && clientIdEncoded != null) {
                clientIdEncoded = Base64Url.encode(clientIdEncoded.getBytes(StandardCharsets.UTF_8));
            }
            String encodedState = state + "." + tabId + "." + clientIdEncoded;
            if (clientData != null) {
                encodedState = encodedState + "." + clientData;
            }
    
            return new IdentityBrokerState(state, clientClientId, tabId, clientData, encodedState);
        }
    
        private static String tryEncodeUuid(String clientId) {
            try {
                UUID clientDbUuid = UUID.fromString(clientId);
                ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
                bb.putLong(clientDbUuid.getMostSignificantBits());
                bb.putLong(clientDbUuid.getLeastSignificantBits());
                return Base64Url.encode(bb.array());
            } catch (RuntimeException e) {
                return null;
            }
        }
}
