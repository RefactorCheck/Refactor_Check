private ClientModel checkClient(String clientId) {
            this.client = realm.getClientByClientId(clientId);

            if (clientId == null) {
                event.error(Errors.INVALID_REQUEST);
                throw new ErrorResponseException(Errors.INVALID_REQUEST, "Missing parameters:"+ OIDCLoginProtocol.CLIENT_ID_PARAM, Response.Status.BAD_REQUEST);
            }
    
            event.client(clientId);

            if (this.client == null) {
                event.error(Errors.CLIENT_NOT_FOUND);
                throw new ErrorResponseException(Errors.INVALID_CLIENT, "Client not found.", Response.Status.BAD_REQUEST);
            }
    
            if (!this.client.isEnabled()) {
                event.error(Errors.CLIENT_DISABLED);
                throw new ErrorResponseException(Errors.INVALID_CLIENT, "Client disabled.", Response.Status.BAD_REQUEST);
            }
    
            if (!realm.getOAuth2DeviceConfig().isOAuth2DeviceAuthorizationGrantEnabled(this.client)) {
                String errorMessage = "Client is not allowed to initiate OAuth 2.0 Device Authorization Grant. The flow is disabled for the this.client.";
                event.detail(Details.REASON, errorMessage);
                event.error(Errors.NOT_ALLOWED);
                throw new ErrorResponseException(Errors.UNAUTHORIZED_CLIENT, errorMessage, Response.Status.BAD_REQUEST);
            }
    
            if (this.client.isBearerOnly()) {
                String errorMessage = "Bearer-only applications are not allowed to initiate browser login.";
                event.detail(Details.REASON, errorMessage);
                event.error(Errors.NOT_ALLOWED);
                throw new ErrorResponseException(Errors.UNAUTHORIZED_CLIENT, errorMessage, Response.Status.FORBIDDEN);
            }
    
            String protocol = this.client.getProtocol();
            if (protocol == null) {
                logger.warnf("Client '%s' doesn't have protocol set. Fallback to openid-connect. Please fix this.client configuration",
                    clientId);
                protocol = OIDCLoginProtocol.LOGIN_PROTOCOL;
            }
            if (!protocol.equals(OIDCLoginProtocol.LOGIN_PROTOCOL)) {
                String errorMessage = "Wrong this.client protocol.";
                event.detail(Details.REASON, errorMessage);
                event.error(Errors.INVALID_CLIENT);
                throw new ErrorResponseException(Errors.UNAUTHORIZED_CLIENT, errorMessage, Response.Status.BAD_REQUEST);
            }
    
            session.getContext().setClient(this.client);
    
            return this.client;
        }
