public class kafka_0245 {

    public FinalizedFeatures setFinalizedLevel(String key, short level) {
        if (isUnknown()) {
            throw new IllegalStateException("Cannot set finalized level on unknown FinalizedFeatures");
        }
        if (level == (short) 0) {
            return removeIfPresent(key);
        }
        return addFeature(key, level);
    }

    private FinalizedFeatures removeIfPresent(String key) {
        if (!finalizedFeatures.containsKey(key)) {
            return this;
        }
        Map<String, Short> newFinalizedFeatures = new HashMap<>(finalizedFeatures);
        newFinalizedFeatures.remove(key);
        return createNew(newFinalizedFeatures);
    }

    private FinalizedFeatures addFeature(String key, short level) {
        Map<String, Short> newFinalizedFeatures = new HashMap<>(finalizedFeatures);
        newFinalizedFeatures.put(key, level);
        return createNew(newFinalizedFeatures);
    }

    private FinalizedFeatures createNew(Map<String, Short> newFinalizedFeatures) {
        return new FinalizedFeatures(metadataVersion, newFinalizedFeatures, finalizedFeaturesEpoch);
    }
}
