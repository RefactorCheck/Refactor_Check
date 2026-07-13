public class keycloak_0121 {

    private static final String CLIENTS_CONFIG_KEY = "clients";

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        factory.register(event -> {
            if (event instanceof ClientRemovedEvent) {
                handleClientRemovedEvent((ClientRemovedEvent) event);
            }
        });
    }

    private void handleClientRemovedEvent(ClientRemovedEvent event) {
        KeycloakSession keycloakSession = event.getKeycloakSession();
        AuthorizationProvider provider = keycloakSession.getProvider(AuthorizationProvider.class);
        StoreFactory storeFactory = provider.getStoreFactory();
        PolicyStore policyStore = storeFactory.getPolicyStore();
        ClientModel removedClient = event.getClient();
        ResourceServerStore resourceServerStore = storeFactory.getResourceServerStore();
        ResourceServer resourceServer = resourceServerStore.findByClient(removedClient);

        if (resourceServer != null) {
            processPolicies(policyStore, resourceServer, removedClient);
        }
    }

    private void processPolicies(PolicyStore policyStore, ResourceServer resourceServer, ClientModel removedClient) {
        policyStore.findByType(resourceServer, getId()).forEach(policy -> {
            List<String> clients = new ArrayList<>();

            for (String clientId : getClients(policy)) {
                if (!clientId.equals(removedClient.getId())) {
                    clients.add(clientId);
                }
            }

            try {
                if (clients.isEmpty()) {
                    policyStore.delete(policy.getId());
                } else {
                    policy.putConfig(CLIENTS_CONFIG_KEY, JsonSerialization.writeValueAsString(clients));
                }
            } catch (IOException e) {
                throw new RuntimeException("Error while synchronizing clients with policy [" + policy.getName() + "].", e);
            }
        });
    }
}
