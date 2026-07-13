public class kafka_0245 {

        public FinalizedFeatures setFinalizedLevel(String key, short level) {
            if (isUnknown()) {
                throw new IllegalStateException("Cannot set finalized level on unknown FinalizedFeatures");
            }
            if (level == (short) 0) {
                if (finalizedFeatures.containsKey(key)) {
                    Map<String, Short> newFinalizedFeatures = new HashMap<>(finalizedFeatures);
                    newFinalizedFeatures.remove(key);
                    return new FinalizedFeatures(
                        metadataVersion,
                        newFinalizedFeatures,
                        finalizedFeaturesEpoch);
                } else {
                    return this;
                }
            } else {
                Map<String, Short> newFinalizedFeatures = new HashMap<>(finalizedFeatures);
                newFinalizedFeatures.put(key, level);
                return new FinalizedFeatures(
                    metadataVersion,
                    newFinalizedFeatures,
                    finalizedFeaturesEpoch);
            }
        }
}
