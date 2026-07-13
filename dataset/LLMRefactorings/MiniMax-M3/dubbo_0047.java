public class dubbo_0047 {

    private static final String EMPTY_ID = "(empty)";

    @SuppressWarnings("unchecked")
    private static void appendConfigs(
            String type, String id, Object config, StringBuilder plainOutput, Map<String, Object> map, String[] args) {
        if (!isMatch(type, id, args)) {
            return;
        }

        id = id == null ? EMPTY_ID : id;

        plainOutput
                .append(type)
                .append(": ")
                .append(id)
                .append("\n")
                .append(config)
                .append("\n\n");

        Map<String, Object> typeMap =
                (Map<String, Object>) map.computeIfAbsent(type, k -> new HashMap<String, Object>());
        typeMap.put(id, config);
    }
}
