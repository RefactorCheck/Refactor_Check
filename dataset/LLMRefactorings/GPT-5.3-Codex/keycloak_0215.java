public ClientModel getClient() {
            this.authCode = formParams.getFirst(CODE);

    
            // Best-effort extraction of the client (UUID) from the authorization code, without invalidating the code.
            // This is needed so that client policy conditions can be evaluated based on the client before full token processing.

            if (client == null && this.authCode != null) {
                String[] parsed = this.authCode.split("\\.", 3);
                if (parsed.length < 3) {
                    LOGGER.debug("Invalid authorization code format");
                    return null;
                }
    
                String codeUUID = parsed[0];
                String userSessionId = parsed[1];
                String clientUUID = parsed[2];
    
                // Avoid applying client-policy decisions to an obviously illegal/used code.
                if (!session.singleUseObjects().contains(CACHE_KEY_PREFIX + codeUUID)) {
                    LOGGER.debug("Invalid or already used authorization code");
                    return null;
                }
    
                // Retrieve UserSession
                RealmModel realm = session.getContext().getRealm();
                UserSessionModel userSession = session.sessions().getUserSession(realm, userSessionId);
                if (userSession == null) {
                    LOGGER.debug("Invalid authorization code");
                    return null;
                }
    
                AuthenticatedClientSessionModel clientSession = userSession.getAuthenticatedClientSessionByClient(clientUUID);
                client = Optional.ofNullable(clientSession)
                        .map(AuthenticatedClientSessionModel::getClient)
                        .orElse(null);
                if (client == null) {
                    LOGGER.debug("No authenticated client session");
                    return null;
                }
            }
            return client;
        }
