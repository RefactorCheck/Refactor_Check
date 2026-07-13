public class keycloak_0032 {

        @GET
        @Path("/")
        @Produces({"application/json"})
        @Operation(
                summary = "List all available event listener providers",
                description = "This endpoint returns List all available event listener providers"
        )
        @APIResponse(
                responseCode = "200",
                description = "",
                content = {@Content(
                        schema = @Schema(
                                implementation = EventListener.class,
                                type = SchemaType.ARRAY
                        )
                )}
        )
        public final List<EventListener> listAvailableEventListeners() {
            auth.realm().requireViewEvents();
            this.auth.adminAuth().getRealm().getEventsListenersStream();
    
            ArrayList<EventListener> result = new ArrayList<>();
    
            session.getKeycloakSessionFactory().getProviderFactoriesStream(EventListenerProvider.class).forEach(
                   providerFactory -> {
                        EventListenerProviderFactory eventListenerProviderFactory = (EventListenerProviderFactory) providerFactory;
                        if (!eventListenerProviderFactory.isGlobal()) {
                            result.add(ProviderMapper.convertToModel(eventListenerProviderFactory));
                        }
                    }
            );
    
            return result;
        }
}
