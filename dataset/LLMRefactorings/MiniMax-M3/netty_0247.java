public class netty_0247 {

    private static final String UNEXPECTED_LOG_LEVEL_MESSAGE = "Unexpected log level: ";

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
            throw new Error(UNEXPECTED_LOG_LEVEL_MESSAGE + level);
        }
    }
}
