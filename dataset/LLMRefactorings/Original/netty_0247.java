public class netty_0247 {

        @Override
        public void log(InternalLogLevel level, String format, Object argA, Object argB) {
            switch (level) {
            case TRACE:
                trace(format, argA, argB);
                break;
            case DEBUG:
                debug(format, argA, argB);
                break;
            case INFO:
                info(format, argA, argB);
                break;
            case WARN:
                warn(format, argA, argB);
                break;
            case ERROR:
                error(format, argA, argB);
                break;
            default:
                throw new Error("Unexpected log level: " + level);
            }
        }
}
