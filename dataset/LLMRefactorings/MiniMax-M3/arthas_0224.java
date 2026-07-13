public class arthas_0224 {

        @SuppressWarnings("unchecked")
        private String extractRelatedTaskId(Object requestParams) {
            if (requestParams == null) {
                return null;
            }
            try {
                if (requestParams instanceof Map) {
                    return extractTaskIdFromMap((Map<String, Object>) requestParams);
                }
            } catch (ClassCastException e) {
                logger.debug("Failed to extract related task ID: {}", e.getMessage());
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        private String extractTaskIdFromMap(Map<String, Object> params) {
            Object meta = params.get("_meta");
            if (meta instanceof Map) {
                Map<String, Object> metaMap = (Map<String, Object>) meta;
                Object relatedTask = metaMap.get(RELATED_TASK_META_KEY);
                if (relatedTask instanceof Map) {
                    Map<String, Object> relatedTaskMap = (Map<String, Object>) relatedTask;
                    return (String) relatedTaskMap.get("taskId");
                }
            }
            return null;
        }
}
