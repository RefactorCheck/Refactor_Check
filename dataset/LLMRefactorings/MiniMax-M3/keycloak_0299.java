public class keycloak_0299 {

    public static long parseDurationMillis(String value, long defaultMillis) {
        try {
            String trimmed = value.trim().toLowerCase();
            if (trimmed.endsWith("ms")) {
                return parseNumber(trimmed, 2);
            }
            if (trimmed.endsWith("s")) {
                return Duration.ofSeconds(parseNumber(trimmed, 1)).toMillis();
            }
            if (trimmed.endsWith("m")) {
                return Duration.ofMinutes(parseNumber(trimmed, 1)).toMillis();
            }
            if (trimmed.endsWith("h")) {
                return Duration.ofHours(parseNumber(trimmed, 1)).toMillis();
            }
            if (trimmed.endsWith("d")) {
                return Duration.ofDays(parseNumber(trimmed, 1)).toMillis();
            }
            return Duration.ofSeconds(Long.parseLong(trimmed)).toMillis();
        } catch (NumberFormatException e) {
            log.warnf("Invalid interval '%s' — falling back to default %dms", value, defaultMillis);
            return defaultMillis;
        }
    }

    private static long parseNumber(String trimmed, int suffixLength) {
        return Long.parseLong(trimmed.substring(0, trimmed.length() - suffixLength).trim());
    }
}
