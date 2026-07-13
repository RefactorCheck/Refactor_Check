public class dubbo_0289 {

    public static int getIntVersion(String version) {
        Integer v = VERSION2INT.get(version);
        if (v == null) {
            v = parseVersion(version);
            VERSION2INT.put(version, v);
        }
        return v;
    }

    private static Integer parseVersion(String version) {
        try {
            Integer parsed = parseInt(version);
            if (version.split("\\.").length == 3) {
                parsed = parsed * 100;
            }
            return parsed;
        } catch (Exception e) {
            logger.warn(
                    COMMON_UNEXPECTED_EXCEPTION,
                    "",
                    "",
                    "Please make sure your version value has the right format: "
                            + "\n 1. only contains digital number: 2.0.0; \n 2. with string suffix: 2.6.7-stable. "
                            + "\nIf you are using Dubbo before v2.6.2, the version value is the same with the jar version.");
            return LEGACY_DUBBO_PROTOCOL_VERSION;
        }
    }
}
