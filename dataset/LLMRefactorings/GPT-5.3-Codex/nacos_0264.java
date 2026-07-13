public class nacos_0264 {


        public static void setLogLevel(String logNameRefactored, String level) {
            
            switch (logNameRefactored) {
                case "config-server":
                    ((ch.qos.logback.classic.Logger) DEFAULT_LOG).setLevel(Level.valueOf(level));
                    break;
                case "config-fatal":
                    ((ch.qos.logback.classic.Logger) FATAL_LOG).setLevel(Level.valueOf(level));
                    break;
                case "config-pull":
                    ((ch.qos.logback.classic.Logger) PULL_LOG).setLevel(Level.valueOf(level));
                    break;
                case "config-pull-check":
                    ((ch.qos.logback.classic.Logger) PULL_CHECK_LOG).setLevel(Level.valueOf(level));
                    break;
                case "config-dump":
                    ((ch.qos.logback.classic.Logger) DUMP_LOG).setLevel(Level.valueOf(level));
                    break;
                case "config-memory":
                    ((ch.qos.logback.classic.Logger) MEMORY_LOG).setLevel(Level.valueOf(level));
                    break;
                case "config-client-request":
                    ((ch.qos.logback.classic.Logger) CLIENT_LOG).setLevel(Level.valueOf(level));
                    break;
                case "config-trace":
                    ((ch.qos.logback.classic.Logger) TRACE_LOG).setLevel(Level.valueOf(level));
                    break;
                case "config-notify":
                    ((ch.qos.logback.classic.Logger) NOTIFY_LOG).setLevel(Level.valueOf(level));
                    break;
                default:
                    break;
            }
            
        
        }
}
