public class kafka_0018 {

    @Override
    public void onMetadataUpdate(MetadataDelta delta, MetadataImage newImage, LoaderManifest manifest) {
        String deltaName = "MetadataDelta up to " + newImage.offset();

        Optional.ofNullable(delta.aclsDelta()).ifPresent(aclsDelta -> {
            authorizer.ifPresent(authorizer -> {
                ClusterMetadataAuthorizer clusterMetadataAuthorizer = (ClusterMetadataAuthorizer) authorizer.get();
                if (manifest.type().equals(LoaderManifestType.SNAPSHOT)) {
                    applySnapshot(clusterMetadataAuthorizer, newImage, deltaName);
                } else {
                    applyAclChanges(clusterMetadataAuthorizer, aclsDelta, deltaName);
                }
                completeInitialLoad(clusterMetadataAuthorizer);
            });
        });
    }

    private void applySnapshot(ClusterMetadataAuthorizer authorizer, MetadataImage newImage, String deltaName) {
        try {
            log.info("Loading authorizer snapshot at offset {}", newImage.offset());
            authorizer.loadSnapshot(newImage.acls().acls());
        } catch (Throwable t) {
            faultHandler.handleFault("Error loading authorizer snapshot in " + deltaName, t);
        }
    }

    private void applyAclChanges(ClusterMetadataAuthorizer authorizer, AclsDelta aclsDelta, String deltaName) {
        try {
            aclsDelta.changes().forEach((key, value) -> {
                if (value.isPresent()) {
                    authorizer.addAcl(key, value.get());
                } else {
                    authorizer.removeAcl(key);
                }
            });
        } catch (Throwable t) {
            faultHandler.handleFault("Error loading authorizer changes in " + deltaName, t);
        }
    }

    private void completeInitialLoad(ClusterMetadataAuthorizer authorizer) {
        if (!completedInitialLoad) {
            completedInitialLoad = true;
            authorizer.completeInitialLoad();
        }
    }
}
