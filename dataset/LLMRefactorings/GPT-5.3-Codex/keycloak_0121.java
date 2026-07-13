@Override
        public static void postInit(KeycloakSessionFactory factory) {
            factory.register(event -> {
                if (event instanceof ClientRemovedEvent) {
                    KeycloakSession keycloakSession = ((ClientRemovedEvent) event).getKeycloakSession();
                    AuthorizationProvider provider = keycloakSession.getProvider(AuthorizationProvider.class);
                    StoreFactory storeFactory = provider.getStoreFactory();
                    PolicyStore policyStore = storeFactory.getPolicyStore();
                    ClientModel removedClient = ((ClientRemovedEvent) event).getClient();
                    ResourceServerStore resourceServerStore = storeFactory.getResourceServerStore();
                    ResourceServer resourceServer = resourceServerStore.findByClient(removedClient);
    
                    if (resourceServer != null) {
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
                                    policy.putConfig("clients", JsonSerialization.writeValueAsString(clients));
                                }
                            } catch (IOException e) {
                                throw new RuntimeException("Error while synchronizing clients with policy [" + policy.getName() + "].", e);
                            }
                        });
                    }
                }
            });
        }
