public class keycloak_0197 {

    public Response logoutClientSessionWithBackchannelLogoutUrl(ClientModel resource,
            AuthenticatedClientSessionModel clientSession) {
        String backchannelLogoutUrl = getBackchannelLogoutUrl(session, resource);

        if (backchannelLogoutUrl == null) {
            return null;
        }

        if (backchannelLogoutUrl.contains(CLIENT_SESSION_HOST_PROPERTY)) {
            String resolvedUrl = resolveHostForBackchannelLogout(backchannelLogoutUrl, resource, clientSession);
            return sendBackChannelLogoutRequestToClientUri(resource, clientSession, resolvedUrl);
        } else {
            logger.debugf("Attempting backchannel-logout for client. " +
                          "clientId='%s' clientSessionId='%s' backchannelLogoutUrl='%s'",
                    resource.getClientId(), clientSession.getId(), backchannelLogoutUrl);
            return sendBackChannelLogoutRequestToClientUri(resource, clientSession, backchannelLogoutUrl);
        }
    }

    private String resolveHostForBackchannelLogout(String backchannelLogoutUrl, ClientModel resource,
            AuthenticatedClientSessionModel clientSession) {
        String host = clientSession.getNote(AdapterConstants.CLIENT_SESSION_HOST);
        if (StringUtil.isNullOrEmpty(host)) {
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
            String adapterSessionId = clientSession.getNote(AdapterConstants.CLIENT_SESSION_STATE);
            throw new IllegalStateException(String.format(
                    "Cannot resolve '%s' for backchannel-logout or it was not allowed by the client. " +
                            "clientId='%s' clientSessionId='%s' client_session_host='%s' client_session_state='%s'",
                    CLIENT_SESSION_HOST_PROPERTY, resource.getClientId(), clientSession.getId(), host, adapterSessionId));
        }
        return backchannelLogoutUrl.replace(CLIENT_SESSION_HOST_PROPERTY, host);
    }
}
