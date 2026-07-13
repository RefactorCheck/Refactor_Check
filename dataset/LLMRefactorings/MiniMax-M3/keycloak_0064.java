public class keycloak_0064 {

        public void update(final ResourceRepresentation resource) {
            if (resource.getId() == null) {
                throw new IllegalArgumentException("You must provide the resource id");
            }
    
            final String endpoint = serverConfiguration.getResourceRegistrationEndpoint() + "/" + encodePathAsIs(resource.getId());
    
            Callable callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    http.<ResourceRepresentation>put(endpoint)
                            .authorizationBearer(pat.call())
                            .json(JsonSerialization.writeValueAsBytes(resource)).execute();
                    return null;
                }
            };
            try {
                callable.call();
            } catch (Exception cause) {
                Throwables.retryAndWrapExceptionIfNecessary(callable, pat, "Could not update resource", cause);
            }
        }
}
