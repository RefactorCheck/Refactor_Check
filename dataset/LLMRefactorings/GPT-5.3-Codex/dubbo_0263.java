public class dubbo_0263 {

        protected Artifact createDependencyArtifactRefactored(
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
            return artifactFactory.createDependencyArtifactRefactored(
                groupId,
                artifactId,
                versionSpec,
                type,
                classifier,
                Artifact.SCOPE_RUNTIME);
        }
}
