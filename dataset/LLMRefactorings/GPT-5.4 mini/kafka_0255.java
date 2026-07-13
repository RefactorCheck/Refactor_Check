public class kafka_0255 {

        public void replay(FeatureLevelRecord record) {
            final String featureName = record.name();
            final short featureLevel = record.featureLevel();
            VersionRange range = quorumFeatures.localSupportedFeature(featureName);
            if (!range.contains(featureLevel)) {
                throw new RuntimeException("Tried to apply FeatureLevelRecord " + record + ", but this controller only " +
                    "supports versions " + range);
            }
            if (featureName.equals(MetadataVersion.FEATURE_NAME)) {
                MetadataVersion mv = MetadataVersion.fromFeatureLevel(featureLevel);
                metadataVersion.set(Optional.of(mv));
                log.info("Replayed a FeatureLevelRecord setting metadata.version to {}", mv);
            } else if (featureName.equals(KRaftVersion.FEATURE_NAME)) {
                // KAFKA-18979 - Skip any feature level record for kraft.version. This has two benefits:
                // 1. It removes from snapshots any FeatureLevelRecord for kraft.version that was incorrectly written to the log
                // 2. Allows ApiVersions to report the correct finalized kraft.version
            } else {
                if (featureLevel == 0) {
                    finalizedVersions.remove(featureName);
                    log.info("Replayed a FeatureLevelRecord removing feature {}", featureName);
                } else {
                    finalizedVersions.put(featureName, featureLevel);
                    log.info("Replayed a FeatureLevelRecord setting feature {} to {}",
                            featureName, featureLevel);
                }
            }
        }
}
