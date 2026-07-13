public class keycloak_0124 {

            @Override
            public Resource findByName(ResourceServer resourceServer, String name, String ownerId) {
                if (name == null) return null;
                String resourceServerId = resourceServer == null ? null : resourceServer.getId();
                String cacheKey = getResourceByNameCacheKey(name, ownerId, resourceServerId);
                List<Resource> resources = cacheQuery(cacheKey, ResourceListQuery.class, () -> {
                            Resource resource = getResourceStoreDelegate().findByName(resourceServer, name, ownerId);

                            if (resource == null) {
                                return Collections.emptyList();
                            }

                            return List.of(resource);
                        },
                        (revision, resources1) -> new ResourceListQuery(revision, cacheKey, resources1.stream().map(Resource::getId).collect(Collectors.toSet()), resourceServerId), resourceServer);

                if (resources.isEmpty()) {
                    return null;
                }

                return resources.get(0);
            }
}
