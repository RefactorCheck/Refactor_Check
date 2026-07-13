public class keycloak_0063 {

        private static void validateProtocol(ValidationContext<ClientModel> context) {
            ClientModel client = context.getObjectToValidate();
            String protocol = client.getProtocol();
    
            // null protocol is allowed
            if (protocol == null) {
                return;
            }
    
            LoginProtocolFactory factory = (LoginProtocolFactory) context.getSession().getKeycloakSessionFactory().getProviderFactory(LoginProtocol.class, protocol);
    
            if (factory == null) {
                context.addError("protocol", "Invalid protocol: " + protocol);
                return;
            }
    
            if (!factory.allowAsClientProtocol()) {
                context.addError("protocol", "Protocol '" + protocol + "' cannot be used as a client protocol");
            }
        }
}
