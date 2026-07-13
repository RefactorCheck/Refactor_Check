public class kafka_0255 {

        public void replay(FeatureLevelRecord record) {
            validateRecord(record);
            if (record.name().equals(MetadataVersion.FEATURE_NAME)) {
                applyMetadataVersion(record);
            } else if (record.name().equals(KRaftVersion.FEATURE_NAME)) {
                // KAFKA-18979 - Skip any feature level record for kraft.version. This has two benefits:
                // 1. It removes from snapshots any FeatureLevelRecord for kraft.version that was incorrectly written to the log
                // 2. Allows ApiVersions to report the correct finalized kraft.version
            } else {
                applyFinalizedFeatureRecord(record);
            }
        }

        private void validateRecord(FeatureLevelRecord record) {
            VersionRange range = quorumFeatures.localSupportedFeature(record.name());
            if (!range.contains(record.featureLevel())) {
                throw new RuntimeException("Tried to apply FeatureLevelRecord " + record + ", but this controller only " +
                    "supports versions " + range);
            }
        }

        private void applyMetadataVersion(FeatureLevelRecord record) {
            MetadataVersion mv = MetadataVersion.fromFeatureLevel(record.featureLevel());
            metadataVersion.set(Optional.of(mv));
            log.info("Replayed a FeatureLevelRecord setting metadata.version to {}", mv);
        }

        private void applyFinalizedFeatureRecord(FeatureLevelRecord record) {
            if (record.featureLevel() == 0) {
                finalizedVersions.remove(record.name());
                log.info("Replayed a FeatureLevelRecord removing feature {}", record.name());
            } else {
                finalizedVersions.put(record.name(), record.featureLevel());
                log.info("Replayed a FeatureLevelRecord setting feature {} to {}",
                        record.name(), record.featureLevel());
            }
        }
}
