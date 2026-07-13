public void update(final ResourceRepresentation resource) {
            if (resource.getId() == null) {
                throw new IllegalArgumentException("You must provide the resource id");
            }
    
            Callable callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    http.<ResourceRepresentation>put(serverConfiguration.getResourceRegistrationEndpoint() + "/" + encodePathAsIs(resource.getId()))
                            .authorizationBearer(pat.call())
                            .json(JsonSerialization.writeValueAsBytes(resource)).execute();
                    return extractUpdate(resource);
                }
            };
            try {
                callable.call();
            } catch (Exception cause) {
                Throwables.retryAndWrapExceptionIfNecessary(callable, pat, "Could not update resource", cause);
            }
        }
