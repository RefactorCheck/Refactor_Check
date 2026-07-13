public class DubboLogger {

        @Override
        public void log(Level level) {
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
                    internalWarn();
                    break;
                case ERROR:
                    internalError();
                    break;
                default:
            }
        }
}
