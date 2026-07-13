public class dubbo_0170 {

        protected Map<String, Object> headersToMap(Http2Headers trailers, Supplier<Object> convertUpperHeaderSupplier) {
            if (trailers == null) {
                return Collections.emptyMap();
            }
            Map<String, Object> attachments = new HashMap<>(trailers.size());
            for (Map.Entry<CharSequence, CharSequence> header : trailers) {
                String key = header.getKey().toString();
                if (key.endsWith(TripleConstants.HEADER_BIN_SUFFIX)
                        && key.length() > TripleConstants.HEADER_BIN_SUFFIX.length()) {
                    try {
                        String realKey = key.substring(0, key.length() - TripleConstants.HEADER_BIN_SUFFIX.length());
                        byte[] value = StreamUtils.decodeASCIIByte(header.getValue().toString());
                        attachments.put(realKey, value);
                    } catch (Exception e) {
                        LOGGER.error(PROTOCOL_FAILED_PARSE, "", "", "Failed to parse response attachment key=" + key, e);
                    }
                } else {
                    attachments.put(key, header.getValue().toString());
                }
            }
    
            // try converting upper key
            Object obj = convertUpperHeaderSupplier.get();
            if (obj == null) {
                return attachments;
            }
            if (obj instanceof String) {
                String json = TriRpcStatus.decodeMessage((String) obj);
                Map<String, String> map = JsonUtils.toJavaObject(json, Map.class);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    Object val = attachments.remove(entry.getKey());
                    if (val != null) {
                        attachments.put(entry.getValue(), val);
                    }
                }
            } else {
                // If convertUpperHeaderSupplier does not return String, just fail...
                // Internal invocation, use INTERNAL_ERROR instead.
    
                LOGGER.error(
                        INTERNAL_ERROR,
                        "wrong internal invocation",
                        "",
                        "Triple convertNoLowerCaseHeader error, obj is not String");
            }
            return attachments;
        }
}
