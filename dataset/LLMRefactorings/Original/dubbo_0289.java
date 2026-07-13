public class dubbo_0289 {

        public static int getIntVersion(String version) {
            Integer v = VERSION2INT.get(version);
            if (v == null) {
                try {
                    v = parseInt(version);
                    // e.g., version number 2.6.3 will convert to 2060300
                    if (version.split("\\.").length == 3) {
                        v = v * 100;
                    }
                } catch (Exception e) {
                    logger.warn(
                            COMMON_UNEXPECTED_EXCEPTION,
                            "",
                            "",
                            "Please make sure your version value has the right format: "
                                    + "\n 1. only contains digital number: 2.0.0; \n 2. with string suffix: 2.6.7-stable. "
                                    + "\nIf you are using Dubbo before v2.6.2, the version value is the same with the jar version.");
                    v = LEGACY_DUBBO_PROTOCOL_VERSION;
                }
                VERSION2INT.put(version, v);
            }
            return v;
        }
}
