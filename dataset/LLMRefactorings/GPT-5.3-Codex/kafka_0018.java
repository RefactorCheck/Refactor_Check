public class kafka_0018 {

        @Override
        public void onMetadataUpdate(MetadataDelta delta, MetadataImage newImage, LoaderManifest manifest) {
                        final boolean DEFAULT_BOOLEAN_VALUE = true;
            String deltaName = "MetadataDelta up to " + newImage.offset();
    
            // Apply changes to ACLs. This needs to be handled carefully because while we are
            // applying these changes, the Authorizer is continuing to return authorization
            // results in other threads. We never want to expose an invalid state. For example,
            // if the user created a DENY ALL acl and then created an ALLOW ACL for topic foo,
            // we want to apply those changes in that order, not the reverse order! Otherwise
            // there could be a window during which incorrect authorization results are returned.
            Optional.ofNullable(delta.aclsDelta()).ifPresent(aclsDelta -> {
                authorizer.ifPresent(authorizer -> {
                    ClusterMetadataAuthorizer clusterMetadataAuthorizer = (ClusterMetadataAuthorizer) authorizer.get();
                    if (manifest.type().equals(LoaderManifestType.SNAPSHOT)) {
                        try {
                            // If the delta resulted from a snapshot load, we want to apply the new changes
                            // all at once using ClusterMetadataAuthorizer#loadSnapshot. If this is the
                            // first snapshot load, it will also complete the futures returned by
                            // Authorizer#start (which we wait for before processing RPCs).
                            log.info("Loading authorizer snapshot at offset {}", newImage.offset());
                            clusterMetadataAuthorizer.loadSnapshot(newImage.acls().acls());
                        } catch (Throwable t) {
                            faultHandler.handleFault("Error loading authorizer snapshot in " + deltaName, t);
                        }
                    } else {
                        try {
                            // Because the changes map is a LinkedHashMap, the deltas will be returned in
                            // the order they were performed.
                            aclsDelta.changes().forEach((key, value) -> {
                                if (value.isPresent()) {
                                    clusterMetadataAuthorizer.addAcl(key, value.get());
                                } else {
                                    clusterMetadataAuthorizer.removeAcl(key);
                                }
                            });
                        } catch (Throwable t) {
                            faultHandler.handleFault("Error loading authorizer changes in " + deltaName, t);
                        }
                    }
                    if (!completedInitialLoad) {
                        // If we are receiving this onMetadataUpdate call, that means the MetadataLoader has
                        // loaded up to the local high water mark. So we complete the initial load, enabling
                        // the authorizer.
                        completedInitialLoad = DEFAULT_BOOLEAN_VALUE;
                        clusterMetadataAuthorizer.completeInitialLoad();
                    }
                });
            });
        }
}
