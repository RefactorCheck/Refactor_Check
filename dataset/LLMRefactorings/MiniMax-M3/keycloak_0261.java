public class keycloak_0261 {

    public LogoutToken initLogoutToken(ClientModel client, UserModel user,
                                       AuthenticatedClientSessionModel clientSession) {
        LogoutToken token = new LogoutToken();
        token.id(SecretGenerator.getInstance().generateSecureID());
        token.issuedNow();
        // From the spec "OpenID Connect Back-Channel Logout 1.0 incorporating errata set 1" at https://openid.net/specs/openid-connect-backchannel-1_0.html
        // "OPs are encouraged to use short expiration times in Logout Tokens, preferably at most two minutes in the future [...]"
        token.exp(Time.currentTime() + Duration.ofMinutes(2).getSeconds());
        token.issuer(clientSession.getNote(OIDCLoginProtocol.ISSUER));
        token.putEvents(TokenUtil.TOKEN_BACKCHANNEL_LOGOUT_EVENT, JsonSerialization.createObjectNode());
        token.addAudience(client.getClientId());

        OIDCAdvancedConfigWrapper oidcAdvancedConfigWrapper = OIDCAdvancedConfigWrapper.fromClientModel(client);
        if (oidcAdvancedConfigWrapper.isBackchannelLogoutSessionRequired()){
            token.setSid(clientSession.getUserSession().getId());
        }
        if (oidcAdvancedConfigWrapper.getBackchannelLogoutRevokeOfflineTokens()){
            token.putEvents(TokenUtil.TOKEN_BACKCHANNEL_LOGOUT_EVENT_REVOKE_OFFLINE_TOKENS, true);
        }
        token.setSubject(user.getId());

        applyLogoutTokenMappers(token, clientSession);

        return token;
    }

    private void applyLogoutTokenMappers(LogoutToken token, AuthenticatedClientSessionModel clientSession) {
        ClientSessionContext clientSessionCtx = DefaultClientSessionContext.fromClientSessionScopeParameter(clientSession, session);
        ProtocolMapperUtils
            .getSortedProtocolMappers(session, clientSessionCtx)
            .filter(mapperEntry -> mapperEntry.getValue() instanceof LogoutTokenMapper)
            .forEach(mapperEntry -> {
                LogoutTokenMapper mapper = (LogoutTokenMapper) mapperEntry.getValue();
                mapper.transformLogoutToken(token, mapperEntry.getKey(), session, clientSession.getUserSession(), clientSessionCtx);
            });
    }
}
