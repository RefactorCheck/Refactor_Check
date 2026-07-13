public class keycloak_0082 {

        private void checkService() {
            if (service == null) {
                throwError("Missing parameter: " + DockerAuthV2Protocol.SERVICE_PARAM,
                        "invalid_request",
                        Errors.INVALID_REQUEST,
                        "service parameter must be provided",
                        Response.Status.BAD_REQUEST);
            }
            event.client(service);
            client = realm.getClientByClientId(service);
            if (client == null) {
                throwError("Client specified by 'service' parameter does not exist",
                        "invalid_client",
                        Errors.CLIENT_NOT_FOUND,
                        "Client specified by 'service' parameter does not exist",
                        Response.Status.BAD_REQUEST);
            }
            if (!client.isEnabled()) {
                throwError("Client specified by 'service' is disabled",
                        "invalid_client",
                        Errors.CLIENT_DISABLED,
                        "Client specified by 'service' is disabled",
                        Response.Status.BAD_REQUEST);
            }
            session.getContext().setClient(client);
        }

        private void throwError(String reason, String errorCode, Errors error, String errorDescription, Response.Status status) {
            event.detail(Details.REASON, reason);
            event.error(error);
            throw new ErrorResponseException(errorCode, errorDescription, status);
        }
}
