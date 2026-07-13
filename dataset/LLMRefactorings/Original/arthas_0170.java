public class arthas_0170 {

        @SuppressWarnings("unchecked")
        public static Object addRelatedTaskMetadata(String taskId, Object notification) {
            if (notification == null || taskId == null) {
                return notification;
            }
            if (notification instanceof McpSchema.TaskStatusNotification) {
                return notification;
            }
            if (notification instanceof Map) {
                Map<String, Object> notifMap = new HashMap<>((Map<String, Object>) notification);
                Map<String, Object> meta = notifMap.containsKey("_meta") && notifMap.get("_meta") instanceof Map
                        ? new HashMap<>((Map<String, Object>) notifMap.get("_meta"))
                        : new HashMap<>();
                Map<String, Object> relatedTask = new HashMap<>();
                relatedTask.put("taskId", taskId);
                meta.put(McpSchema.RELATED_TASK_META_KEY, relatedTask);
                notifMap.put("_meta", meta);
                return notifMap;
            }
            return notification;
        }
}
