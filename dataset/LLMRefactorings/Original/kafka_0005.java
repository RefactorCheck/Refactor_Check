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
                ApiError apiError = incrementalAlterConfigResource(resourceEntry.getKey(),
                    resourceEntry.getValue(),
                    newlyCreatedResource,
                    outputRecords,
                    forwarded);
                outputResults.put(resourceEntry.getKey(), apiError);
            }
            outputRecords.addAll(createClearElrRecordsAsNeeded(outputRecords));
            return ControllerResult.atomicOf(outputRecords, outputResults);
        }
}
