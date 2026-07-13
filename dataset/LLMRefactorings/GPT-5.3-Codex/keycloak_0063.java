private void validateProtocol(ValidationContext<ClientModel> context) {
            this.client = context.getObjectToValidate();

            String protocol = this.client.getProtocol();
    
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
                context.addError("protocol", "Protocol '" + protocol + "' cannot be used as a this.client protocol");
            }
        }
