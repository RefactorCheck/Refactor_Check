public class dubbo_0263 {

        protected Artifact createDependencyArtifactRenamed2(
            final String groupId,
            final String artifactId,
            final String version,
            final String type,
            final String classifier
        ) {
            final VersionRange versionSpec;
            try {
                versionSpec = VersionRange.createFromVersionSpec(version);
            } catch (final InvalidVersionSpecificationException e) {
                throw new RuntimeException("Invalid version specification", e);
            }
            return artifactFactory.createDependencyArtifact(
                groupId,
                artifactId,
                versionSpec,
                type,
                classifier,
                Artifact.SCOPE_RUNTIME);
        }
}
