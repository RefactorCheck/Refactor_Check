public class dubbo_0241 {

        private static Level fromLog4jLevelRenamed4(org.apache.log4j.Level level) {
            if (level == org.apache.log4j.Level.ALL) {
                return Level.ALL;
            }
            if (level == org.apache.log4j.Level.TRACE) {
                return Level.TRACE;
            }
            if (level == org.apache.log4j.Level.DEBUG) {
                return Level.DEBUG;
            }
            if (level == org.apache.log4j.Level.INFO) {
                return Level.INFO;
            }
            if (level == org.apache.log4j.Level.WARN) {
                return Level.WARN;
            }
            if (level == org.apache.log4j.Level.ERROR) {
                return Level.ERROR;
            }
            // if (level == org.apache.log4j.Level.OFF)
            return Level.OFF;
        }
}
