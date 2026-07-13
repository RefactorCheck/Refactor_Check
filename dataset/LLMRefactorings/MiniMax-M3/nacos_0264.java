public class nacos_0264 {

    public static void setLogLevel(String logName, String level) {
        switch (logName) {
            case "config-server":
                setLoggerLevel((ch.qos.logback.classic.Logger) DEFAULT_LOG, level);
                break;
            case "config-fatal":
                setLoggerLevel((ch.qos.logback.classic.Logger) FATAL_LOG, level);
                break;
            case "config-pull":
                setLoggerLevel((ch.qos.logback.classic.Logger) PULL_LOG, level);
                break;
            case "config-pull-check":
                setLoggerLevel((ch.qos.logback.classic.Logger) PULL_CHECK_LOG, level);
                break;
            case "config-dump":
                setLoggerLevel((ch.qos.logback.classic.Logger) DUMP_LOG, level);
                break;
            case "config-memory":
                setLoggerLevel((ch.qos.logback.classic.Logger) MEMORY_LOG, level);
                break;
            case "config-client-request":
                setLoggerLevel((ch.qos.logback.classic.Logger) CLIENT_LOG, level);
                break;
            case "config-trace":
                setLoggerLevel((ch.qos.logback.classic.Logger) TRACE_LOG, level);
                break;
            case "config-notify":
                setLoggerLevel((ch.qos.logback.classic.Logger) NOTIFY_LOG, level);
                break;
            default:
                break;
        }
    }

    private static void setLoggerLevel(ch.qos.logback.classic.Logger logger, String level) {
        logger.setLevel(Level.valueOf(level));
    }
}
