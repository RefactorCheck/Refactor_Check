public class dubbo_0295 {

    public static <V extends Object> Set<String> getSubIds(Collection<Map<String, V>> configMaps, String prefix) {
        if (!prefix.endsWith(".")) {
            prefix += ".";
        }
        Set<String> ids = new LinkedHashSet<>();
        for (Map<String, V> configMap : configMaps) {
            Map<String, V> copy;
            synchronized (configMap) {
                copy = new HashMap<>(configMap);
            }
            for (Map.Entry<String, V> entry : copy.entrySet()) {
                processEntry(entry, prefix, ids);
            }
        }
        return ids;
    }

    private static <V> void processEntry(Map.Entry<String, V> entry, String prefix, Set<String> ids) {
        String key = entry.getKey();
        V val = entry.getValue();
        if (StringUtils.startsWithIgnoreCase(key, prefix)
                && key.length() > prefix.length()
                && !ConfigurationUtils.isEmptyValue(val)) {

            String k = key.substring(prefix.length());
            int endIndex = k.indexOf(".");
            if (endIndex > 0) {
                String id = k.substring(0, endIndex);
                ids.add(id);
            }
        }
    }
}
