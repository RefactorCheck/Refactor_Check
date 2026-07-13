public class keycloak_0066 {

        public static IdentityBrokerState decoded(String state, String clientId, String clientClientId, String tabId, String clientData) {
    
            String clientIdEncoded = clientClientId; // Default use the client.clientId
            boolean isUuid = false;
            if (clientId != null) {
                // According to (http://docs.oasis-open.org/security/saml/v2.0/saml-bindings-2.0-os.pdf) there is a limit on the relaystate of 80 bytes.
                // in order to try to adher to the SAML specification we use an encoded value of the client.id (probably UUID) instead of the with
                // probability bigger client.clientId. If the client.id is not in UUID format we just use the client.clientid as is
                try {
                    UUID clientDbUuid = UUID.fromString(clientId);
                    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
                    bb.putLong(clientDbUuid.getMostSignificantBits());
                    bb.putLong(clientDbUuid.getLeastSignificantBits());
                    byte[] clientUuidBytes = bb.array();
                    clientIdEncoded = Base64Url.encode(clientUuidBytes);
                    isUuid = true;
                } catch (RuntimeException e) {
                    // Ignore...the clientid in the database was not in UUID format. Just use as is.
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
}
