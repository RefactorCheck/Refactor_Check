public class keycloak_0113 {

        public KeyWrapper getPublicKey(String algorithm, String kid) {
            boolean loadedKeysFromServer = false;
            JSONWebKeySet jsonWebKeySet = publicKeys.get(client.getRealm());
            if (jsonWebKeySet == null) {
                jsonWebKeySet = getRealmKeys();
                publicKeys.put(client.getRealm(), jsonWebKeySet);
                loadedKeysFromServer = true;
            }
    
            KeyWrapper key = findKey(jsonWebKeySet, algorithm, kid);
    
            if (key == null && !loadedKeysFromServer) {
                jsonWebKeySet = getRealmKeys();
                publicKeys.put(client.getRealm(), jsonWebKeySet);
    
                key = findKey(jsonWebKeySet, algorithm, kid);
            }
    
            if (key == null) {
                throw new RuntimeException("Public key for realm:" + client.getRealm() + ", algorithm: " + algorithm + " not found");
            }
    
            return key;
        }
}
