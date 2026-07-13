public class kafka_0250 {

        private TaskStatus parseTaskStatus(ConnectorTaskId taskId, byte[] data) {
            try {
                SchemaAndValue schemaAndValue = converter.toConnectData(statusTopic, data);
                if (!(schemaAndValue.value() instanceof Map)) {
                    log.error("Invalid task status type {}", schemaAndValue.value().getClass());
                    return null;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> statusMap = (Map<String, Object>) schemaAndValue.value();
                return createTaskStatus(taskId, statusMap);
            } catch (Exception e) {
                log.error("Failed to deserialize task status", e);
                return null;
            }
        }

        private TaskStatus createTaskStatus(ConnectorTaskId taskId, Map<String, Object> statusMap) {
            TaskStatus.State state = TaskStatus.State.valueOf((String) statusMap.get(STATE_KEY_NAME));
            String trace = (String) statusMap.get(TRACE_KEY_NAME);
            String workerUrl = (String) statusMap.get(WORKER_ID_KEY_NAME);
            int generation = ((Long) statusMap.get(GENERATION_KEY_NAME)).intValue();
            String version = (String) statusMap.get(VERSION_KEY_NAME);
            return new TaskStatus(taskId, state, workerUrl, generation, trace, version);
        }
}
