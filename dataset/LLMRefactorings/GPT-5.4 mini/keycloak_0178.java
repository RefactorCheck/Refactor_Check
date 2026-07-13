public class keycloak_0178 {

        protected void sendClientNotificationRequest(ClientModel client, CibaConfig cibaConfig, OAuth2DeviceCodeModel deviceModel) {
            String clientNotificationEndpoint = cibaConfig.getBackchannelClientNotificationEndpoint(client);
            if (clientNotificationEndpoint == null) {
                handleInvalidClientNotificationEndpoint(client, clientNotificationEndpoint);
            }

            logger.debugf("Sending request to client notification endpoint '%s' for the client '%s'", clientNotificationEndpoint, client.getClientId());

            ClientNotificationEndpointRequest clientNotificationRequest = new ClientNotificationEndpointRequest();
            clientNotificationRequest.setAuthReqId(deviceModel.getAuthReqId());

            SimpleHttpRequest simpleHttp = SimpleHttp.create(session).doPost(clientNotificationEndpoint)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .json(clientNotificationRequest)
                    .auth(deviceModel.getClientNotificationToken());

            try {
                int notificationResponseStatus = simpleHttp.asStatus();

                logger.tracef("Received status '%d' from request to client notification endpoint '%s' for the client '%s'", notificationResponseStatus, clientNotificationEndpoint, client.getClientId());
                if (notificationResponseStatus != 200 && notificationResponseStatus != 204) {
                    logger.warnf("Invalid status returned from client notification endpoint '%s' of client '%s'", clientNotificationEndpoint, client.getClientId());
                    handleInvalidClientNotificationEndpoint(client, clientNotificationEndpoint);
                }
            } catch (IOException ioe) {
                logger.errorf(ioe, "Failed to send request to client notification endpoint '%s' of client '%s'", clientNotificationEndpoint, client.getClientId());
                handleInvalidClientNotificationEndpoint(client, clientNotificationEndpoint);
            }
        }

        private void handleInvalidClientNotificationEndpoint(ClientModel client, String clientNotificationEndpoint) {
            event.error(Errors.INVALID_REQUEST);
            throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "Failed to send request to client notification endpoint",
                    Response.Status.BAD_REQUEST);
        }
}
