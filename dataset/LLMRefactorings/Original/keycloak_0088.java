public class keycloak_0088 {

        @GET
        @Path("clients/{client}")
        @Produces(MediaType.TEXT_HTML_UTF_8)
        public Response idpInitiatedSSO(@PathParam("client") String clientUrlName, @QueryParam("RelayState") String relayState) {
            event.event(EventType.LOGIN);
            CacheControlUtil.noBackButtonCacheControlHeader(session);
            ClientModel client = session.clients()
                    .searchClientsByAttributes(realm, Collections.singletonMap(SamlProtocol.SAML_IDP_INITIATED_SSO_URL_NAME, clientUrlName), 0, 1)
                    .findFirst().orElse(null);
    
            if (client == null) {
                event.error(Errors.CLIENT_NOT_FOUND);
                return ErrorPage.error(session, null, Response.Status.BAD_REQUEST, Messages.CLIENT_NOT_FOUND);
            }
            if (!client.isEnabled()) {
                event.error(Errors.CLIENT_DISABLED);
                return ErrorPage.error(session, null, Response.Status.BAD_REQUEST, Messages.CLIENT_DISABLED);
            }
            if (!isClientProtocolCorrect(client)) {
                event.error(Errors.INVALID_CLIENT);
                return ErrorPage.error(session, null, Response.Status.BAD_REQUEST, "Wrong client protocol.");
            }
    
            session.getContext().setClient(client);
    
            AuthenticationSessionModel authSession = getOrCreateLoginSessionForIdpInitiatedSso(this.session, this.realm, client, relayState);
            if (authSession == null) {
                logger.error("SAML assertion consumer url not set up");
                event.error(Errors.INVALID_REDIRECT_URI);
                return ErrorPage.error(session, null, Response.Status.BAD_REQUEST, Messages.INVALID_REDIRECT_URI);
            }
    
            return newBrowserAuthentication(authSession, false, false);
        }
}
