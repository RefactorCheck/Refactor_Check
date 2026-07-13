public class arthas_0224 {

        @SuppressWarnings("unchecked")
        private String extractRelatedTaskId(Object requestParams) {
            if (requestParams == null) {
                return null;
            }
            try {
                if (requestParams instanceof Map) {
                    Map<String, Object> params = (Map<String, Object>) requestParams;
                    Object meta = params.get("_meta");
                    if (meta instanceof Map) {
                        Map<String, Object> metaMap = (Map<String, Object>) meta;
                        Object relatedTask = metaMap.get(RELATED_TASK_META_KEY);
                        if (relatedTask instanceof Map) {
                            Map<String, Object> relatedTaskMap = (Map<String, Object>) relatedTask;
                            return (String) relatedTaskMap.get("taskId");
                        }
                    }
                }
            } catch (ClassCastException e) {
                logger.debug("Failed to extract related task ID: {}", e.getMessage());
            }
            return null;
        }
}
