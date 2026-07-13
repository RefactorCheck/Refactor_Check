public class keycloak_0263 {

        @Path("providers")
        @GET
        @NoCache
        @Produces(MediaType.APPLICATION_JSON)
        @Tag(name = KeycloakOpenAPI.Admin.Tags.CLIENT_REGISTRATION_POLICY)
        @Operation( summary="Base path for retrieve providers with the configProperties properly filled")
        public Stream<ComponentTypeRepresentation> getProviders() {
            auth.realm().requireViewRealm();
            return session.getKeycloakSessionFactory().getProviderFactoriesStream(ClientRegistrationPolicy.class)
                    .map((ProviderFactory factory) -> {
                        ClientRegistrationPolicyFactory clientRegFactory = (ClientRegistrationPolicyFactory) factory;
                        List<ProviderConfigProperty> configProps = clientRegFactory.getConfigProperties(session);
    
                        ComponentTypeRepresentation rep = new ComponentTypeRepresentation();
                        rep.setId(clientRegFactory.getId());
                        rep.setHelpText(clientRegFactory.getHelpText());
                        rep.setProperties(ModelToRepresentation.toRepresentation(configProps));
                        return rep;
                    });
        }
}
