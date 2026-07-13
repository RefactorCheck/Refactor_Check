public class dubbo_0183 {

    protected void buildParameters(StringBuilder buf, boolean concat, String[] parameters) {
        if (CollectionUtils.isNotEmptyMap(getParameters())) {
            List<String> includes = (ArrayUtils.isEmpty(parameters) ? null : Arrays.asList(parameters));
            boolean first = true;
            for (Map.Entry<String, String> entry : new TreeMap<>(getParameters()).entrySet()) {
                if (StringUtils.isNotEmpty(entry.getKey()) && (includes == null || includes.contains(entry.getKey()))) {
                    if (first) {
                        if (concat) {
                            buf.append('?');
                        }
                        first = false;
                    } else {
                        buf.append('&');
                    }
                    appendParameter(buf, entry);
                }
            }
        }
    }

    private void appendParameter(StringBuilder buf, Map.Entry<String, String> entry) {
        buf.append(entry.getKey());
        buf.append('=');
        buf.append(entry.getValue() == null ? "" : entry.getValue().trim());
    }
}
