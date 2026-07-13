public class keycloak_0299 {

        public static long parseDuration(String value, long defaultMillis) {
            try {
                String trimmed = value.trim().toLowerCase();
                if (trimmed.endsWith("ms")) {
                    return Long.parseLong(trimmed.substring(0, trimmed.length() - 2).trim());
                }
                if (trimmed.endsWith("s")) {
                    return Duration.ofSeconds(Long.parseLong(trimmed.substring(0, trimmed.length() - 1).trim())).toMillis();
                }
                if (trimmed.endsWith("m")) {
                    return Duration.ofMinutes(Long.parseLong(trimmed.substring(0, trimmed.length() - 1).trim())).toMillis();
                }
                if (trimmed.endsWith("h")) {
                    return Duration.ofHours(Long.parseLong(trimmed.substring(0, trimmed.length() - 1).trim())).toMillis();
                }
                if (trimmed.endsWith("d")) {
                    return Duration.ofDays(Long.parseLong(trimmed.substring(0, trimmed.length() - 1).trim())).toMillis();
                }
                return Duration.ofSeconds(Long.parseLong(trimmed)).toMillis();
            } catch (NumberFormatException e) {
                log.warnf("Invalid interval '%s' — falling back to default %dms", value, defaultMillis);
                return defaultMillis;
            }
        }
}
