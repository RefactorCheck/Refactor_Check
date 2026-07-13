public class dubbo_0296 {

        private static void putHeader(BiConsumer<CharSequence, String> consumer, String key, Object value) {
            try {
                if (value instanceof CharSequence || value instanceof Number || value instanceof Boolean) {
                    String str = value.toString();
                    consumer.accept(key, str);
                } else if (value instanceof Date) {
                    consumer.accept(key, DateFormatter.format((Date) value));
                } else if (value instanceof byte[]) {
                    String str = encodeBase64ASCII((byte[]) value);
                    consumer.accept(key + TripleConstants.HEADER_BIN_SUFFIX, str);
                } else {
                    LOGGER.warn(
                            PROTOCOL_UNSUPPORTED,
                            "",
                            "",
                            "Unsupported attachment k: " + key + " class: "
                                    + value.getClass().getName());
                }
            } catch (Throwable t) {
                LOGGER.warn(
                        PROTOCOL_UNSUPPORTED,
                        "",
                        "",
                        "Meet exception when convert single attachment key:" + key + " value=" + value,
                        t);
            }
        }
}
