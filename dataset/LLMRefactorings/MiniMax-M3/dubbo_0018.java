public class dubbo_0018 {

        private List<File> resolvePluginDependencies() {
            List<File> resolvedJars = new ArrayList<>();
            final VersionRange versionSpec;
            try {
                versionSpec = VersionRange.createFromVersionSpec(dubboVersion);
            } catch (InvalidVersionSpecificationException e) {
                throw new RuntimeException("Invalid plugin version specification", e);
            }
            final Artifact protocPluginArtifact =
                artifactFactory.createDependencyArtifact(
                    "org.apache.dubbo",
                    "dubbo-compiler",
                    versionSpec,
                    "jar",
                    "",
                    Artifact.SCOPE_RUNTIME);
            final ArtifactResolutionRequest request = buildArtifactResolutionRequest(protocPluginArtifact);

            final ArtifactResolutionResult result = repositorySystem.resolve(request);

            try {
                resolutionErrorHandler.throwErrors(request, result);
            } catch (ArtifactResolutionException e) {
                throw new RuntimeException("Unable to resolve plugin artifact: " + e.getMessage(), e);
            }

            final Set<Artifact> artifacts = result.getArtifacts();

            if (artifacts == null || artifacts.isEmpty()) {
                throw new RuntimeException("Unable to resolve plugin artifact");
            }

            for (final Artifact artifact : artifacts) {
                resolvedJars.add(artifact.getFile());
            }

            if (getLog().isDebugEnabled()) {
                getLog().debug("Resolved jars: " + resolvedJars);
            }
            return resolvedJars;
        }

        private ArtifactResolutionRequest buildArtifactResolutionRequest(final Artifact protocPluginArtifact) {
            return new ArtifactResolutionRequest()
                .setArtifact(project.getArtifact())
                .setResolveRoot(false)
                .setArtifactDependencies(Collections.singleton(protocPluginArtifact))
                .setManagedVersionMap(emptyMap())
                .setLocalRepository(localRepository)
                .setRemoteRepositories(remoteRepositories)
                .setOffline(session.isOffline())
                .setForceUpdate(session.getRequest().isUpdateSnapshots())
                .setServers(session.getRequest().getServers())
                .setMirrors(session.getRequest().getMirrors())
                .setProxies(session.getRequest().getProxies());
        }
}
