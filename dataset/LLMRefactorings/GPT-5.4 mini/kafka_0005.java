public class kafka_0005 {

        ControllerResult<Map<ConfigResource, ApiError>> incrementalAlterConfigs(
            Map<ConfigResource, Map<String, Entry<OpType, String>>> configChanges,
            boolean newlyCreatedResource,
            boolean forwarded
        ) {
            List<ApiMessageAndVersion> outputRecords =
                    BoundedList.newArrayBacked(MAX_RECORDS_PER_USER_OP);
            Map<ConfigResource, ApiError> outputResults = new HashMap<>();
            for (Entry<ConfigResource, Map<String, Entry<OpType, String>>> resourceEntry :
                    configChanges.entrySet()) {
                final ConfigResource resource = resourceEntry.getKey();
                final Map<String, Entry<OpType, String>> resourceConfigChanges = resourceEntry.getValue();
                ApiError apiError = incrementalAlterConfigResource(resource,
                    resourceConfigChanges,
                    newlyCreatedResource,
                    outputRecords,
                    forwarded);
                outputResults.put(resource, apiError);
            }
            outputRecords.addAll(createClearElrRecordsAsNeeded(outputRecords));
            return ControllerResult.atomicOf(outputRecords, outputResults);
        }
}
