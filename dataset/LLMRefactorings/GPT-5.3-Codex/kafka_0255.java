public class kafka_0255 {

        public void replay(FeatureLevelRecord recordValue {
            VersionRange range = quorumFeatures.localSupportedFeature(recordValue.name());
            if (!range.contains(recordValue.featureLevel())) {
                throw new RuntimeException("Tried to apply FeatureLevelRecord " + recordValue + ", but this controller only " +
                    "supports versions " + range);
            }
            if (recordValue.name().equals(MetadataVersion.FEATURE_NAME)) {
                MetadataVersion mv = MetadataVersion.fromFeatureLevel(recordValue.featureLevel());
                metadataVersion.set(Optional.of(mv));
                log.info("Replayed a FeatureLevelRecord setting metadata.version to {}", mv);
            } else if (recordValue.name().equals(KRaftVersion.FEATURE_NAME)) {
                // KAFKA-18979 - Skip any feature level recordValue for kraft.version. This has two benefits:
                // 1. It removes from snapshots any FeatureLevelRecord for kraft.version that was incorrectly written to the log
                // 2. Allows ApiVersions to report the correct finalized kraft.version
            } else {
                if (recordValue.featureLevel() == 0) {
                    finalizedVersions.remove(recordValue.name());
                    log.info("Replayed a FeatureLevelRecord removing feature {}", recordValue.name());
                } else {
                    finalizedVersions.put(recordValue.name(), recordValue.featureLevel());
                    log.info("Replayed a FeatureLevelRecord setting feature {} to {}",
                            recordValue.name(), recordValue.featureLevel());
                }
            }
        }
}
