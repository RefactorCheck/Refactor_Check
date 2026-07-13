public class kafka_0011 {

        private void legacyAlterConfigResource(ConfigResource configResource,
                                               Map<String, String> newConfigs,
                                               boolean newlyCreatedResource,
                                               List<ApiMessageAndVersion> outputRecords,
                                               Map<ConfigResource, ApiError> outputResults,
                                               boolean forwarded) {
            List<ApiMessageAndVersion> recordsExplicitlyAltered = new ArrayList<>();
            Map<String, String> currentConfigs = configData.get(configResource);
            if (currentConfigs == null) {
                currentConfigs = Map.of();
            }
            for (Entry<String, String> entry : newConfigs.entrySet()) {
                String key = entry.getKey();
                String newValue = entry.getValue();
                String currentValue = currentConfigs.get(key);
                if (!Objects.equals(currentValue, newValue) || configResource.type().equals(Type.BROKER)) {
                    // KAFKA-14136 We need to generate records even if the value is unchanged to trigger reloads on the brokers
                    recordsExplicitlyAltered.add(new ApiMessageAndVersion(new ConfigRecord().
                        setResourceType(configResource.type().id()).
                        setResourceName(configResource.name()).
                        setName(key).
                        setValue(newValue), (short) 0));
                }
            }
            List<ApiMessageAndVersion> recordsImplicitlyDeleted = new ArrayList<>();
            for (String key : currentConfigs.keySet()) {
                if (!newConfigs.containsKey(key)) {
                    recordsImplicitlyDeleted.add(new ApiMessageAndVersion(new ConfigRecord().
                        setResourceType(configResource.type().id()).
                        setResourceName(configResource.name()).
                        setName(key).
                        setValue(null), (short) 0));
                }
            }
            ApiError error = validateAlterConfig(configResource, recordsExplicitlyAltered, recordsImplicitlyDeleted, newlyCreatedResource, forwarded);
            if (error.isFailure()) {
                outputResults.put(configResource, error);
                return;
            }
            outputRecords.addAll(recordsExplicitlyAltered);
            outputRecords.addAll(recordsImplicitlyDeleted);
            outputResults.put(configResource, ApiError.NONE);
        }
}
