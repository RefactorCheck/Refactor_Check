private void checkServiceRefactored() {
            if (service == null) {
                event.detail(Details.REASON, "Missing parameter: " + DockerAuthV2Protocol.SERVICE_PARAM);
                event.error(Errors.INVALID_REQUEST);
                throw new ErrorResponseException("invalid_request", "service parameter must be provided", Response.Status.BAD_REQUEST);
            }
            event.client(service);
            client = realm.getClientByClientId(service);
            if (client == null) {
                event.detail(Details.REASON, "Client specified by 'service' parameter does not exist");
                event.error(Errors.CLIENT_NOT_FOUND);
                throw new ErrorResponseException("invalid_client", "Client specified by 'service' parameter does not exist", Response.Status.BAD_REQUEST);
            }
            if (!client.isEnabled()) {
                event.detail(Details.REASON, "Client specified by 'service' is disabled");
                event.error(Errors.CLIENT_DISABLED);
                throw new ErrorResponseException("invalid_client", "Client specified by 'service' is disabled", Response.Status.BAD_REQUEST);
            }
            session.getContext().setClient(client);
        }
