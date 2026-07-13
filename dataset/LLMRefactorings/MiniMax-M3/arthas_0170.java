public class arthas_0170 {

    private static final String META_KEY = "_meta";
    private static final String TASK_ID_KEY = "taskId";

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
            Map<String, Object> meta = notifMap.containsKey(META_KEY) && notifMap.get(META_KEY) instanceof Map
                    ? new HashMap<>((Map<String, Object>) notifMap.get(META_KEY))
                    : new HashMap<>();
            Map<String, Object> relatedTask = new HashMap<>();
            relatedTask.put(TASK_ID_KEY, taskId);
            meta.put(McpSchema.RELATED_TASK_META_KEY, relatedTask);
            notifMap.put(META_KEY, meta);
            return notifMap;
        }
        return notification;
    }
}
