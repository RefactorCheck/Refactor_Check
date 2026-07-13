public class keycloak_0010 {

        private void validateAcrLoaMap(ValidationContext<ClientModel> context) {
            ClientModel client = context.getObjectToValidate();
            if (!SamlProtocol.LOGIN_PROTOCOL.equals(client.getProtocol()) && !OIDCLoginProtocol.LOGIN_PROTOCOL.equals(client.getProtocol())) {
                return;
            }
            String value = client.getAttribute(Constants.ACR_LOA_MAP);
            if (value != null && StringUtil.isNotBlank(value)) {
                try {
                    Map<String, Integer> map = AcrUtils.parseAcrLoaMap(value);
                    validateSamlAcrLoaUris(client, map);
                } catch (IOException e) {
                    context.addError(Constants.ACR_LOA_MAP, "Invalid client configuration (ACR-LOA map) for client");
                } catch (URISyntaxException e) {
                    context.addError(Constants.ACR_LOA_MAP, "Invalid URI for ACR-LOA map: " + e.getInput());
                }
            }
        }

        private void validateSamlAcrLoaUris(ClientModel client, Map<String, Integer> map) throws URISyntaxException {
            if (SamlProtocol.LOGIN_PROTOCOL.equals(client.getProtocol())) {
                for (String uri : map.keySet()) {
                    new URI(uri);
                }
            }
        }
}
