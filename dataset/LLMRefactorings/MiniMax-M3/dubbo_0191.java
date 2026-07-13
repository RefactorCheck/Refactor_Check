public class dubbo_0191 {

        @Override
        public void log(String message, Level level) {
            switch (level) {
                case TRACE:
                    trace();
                    break;
                case DEBUG:
                    debug();
                    break;
                case INFO:
                    info();
                    break;
                case WARN:
                    warn(message);
                    break;
                case ERROR:
                    error(message);
                    break;
                default:
            }
        }
}
