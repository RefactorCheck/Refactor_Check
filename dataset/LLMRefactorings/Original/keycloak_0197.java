public class keycloak_0197 {

        public Response logoutClientSessionWithBackchannelLogoutUrl(ClientModel resource,
                AuthenticatedClientSessionModel clientSession) {
            String backchannelLogoutUrl = getBackchannelLogoutUrl(session, resource);
    
            if (backchannelLogoutUrl == null) {
                return null;
            }
    
            // Send logout separately to each host (needed for single-sign-out in cluster for non-distributable apps -
            // KEYCLOAK-748)
            if (backchannelLogoutUrl.contains(CLIENT_SESSION_HOST_PROPERTY)) {
                String host = clientSession.getNote(AdapterConstants.CLIENT_SESSION_HOST);
                if (StringUtil.isNullOrEmpty(host)) {
                    // Host placeholder in backchannel logout URL cannot be resolved. Usually the client did not send
                    // both 'client_session_host' and 'client_session_state' on its token request.
                    String adapterSessionId = clientSession.getNote(AdapterConstants.CLIENT_SESSION_STATE);
                    throw new IllegalStateException(String.format(
                            "Cannot resolve '%s' for backchannel-logout. " +
                            "clientId='%s' clientSessionId='%s' client_session_host='%s' client_session_state='%s'",
                            CLIENT_SESSION_HOST_PROPERTY, resource.getClientId(), clientSession.getId(), host, adapterSessionId));
                }
                logger.debugf("Attempting backchannel-logout for client with host from client session. " +
                              "clientId='%s' clientSessionId='%s' host='%s' backchannelLogoutUrl='%s'",
                        resource.getClientId(), clientSession.getId(), host, backchannelLogoutUrl);
    
                if (StringUtil.isNullOrEmpty(host) || !ClientHostUtils.isHostAllowedForClient(host, resource, session)) {
                    // Host placeholder in backchannel logout URL cannot be resolved or is not allowed. Usually the client did not send
                    // both 'client_session_host' and 'client_session_state' on its token request.
                    String adapterSessionId = clientSession.getNote(AdapterConstants.CLIENT_SESSION_STATE);
                    throw new IllegalStateException(String.format(
                            "Cannot resolve '%s' for backchannel-logout or it was not allowed by the client. " +
                                    "clientId='%s' clientSessionId='%s' client_session_host='%s' client_session_state='%s'",
                            CLIENT_SESSION_HOST_PROPERTY, resource.getClientId(), clientSession.getId(), host, adapterSessionId));
                }
                String currentHostMgmtUrl = backchannelLogoutUrl.replace(CLIENT_SESSION_HOST_PROPERTY, host);
                return sendBackChannelLogoutRequestToClientUri(resource, clientSession, currentHostMgmtUrl);
            } else {
                logger.debugf("Attempting backchannel-logout for client. " +
                              "clientId='%s' clientSessionId='%s' backchannelLogoutUrl='%s'",
                        resource.getClientId(), clientSession.getId(), backchannelLogoutUrl);
                return sendBackChannelLogoutRequestToClientUri(resource, clientSession, backchannelLogoutUrl);
            }
        }
}
