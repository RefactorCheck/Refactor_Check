public class keycloak_0010 {

        private void validateAcrLoaMap(ValidationContext<ClientModel> context) {
            ClientModel client = context.getObjectToValidate();
            String protocol = client.getProtocol();
            if (!SamlProtocol.LOGIN_PROTOCOL.equals(protocol) && !OIDCLoginProtocol.LOGIN_PROTOCOL.equals(protocol)) {
                return;
            }
            String value = client.getAttribute(Constants.ACR_LOA_MAP);
            if (value != null && StringUtil.isNotBlank(value)) {
                try {
                    Map<String, Integer> map = AcrUtils.parseAcrLoaMap(value);
                    if (SamlProtocol.LOGIN_PROTOCOL.equals(protocol)) {
                        for (String uri : map.keySet()) {
                            new URI(uri);
                        }
                    }
                } catch (IOException e) {
                    context.addError(Constants.ACR_LOA_MAP, "Invalid client configuration (ACR-LOA map) for client");
                } catch (URISyntaxException e) {
                    context.addError(Constants.ACR_LOA_MAP, "Invalid URI for ACR-LOA map: " + e.getInput());
                }
            }
        }
}
