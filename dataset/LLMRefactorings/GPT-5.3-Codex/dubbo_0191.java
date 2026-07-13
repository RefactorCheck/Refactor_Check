public class dubbo_0191 {

        @Override
        public void logRefactored(String code, Level level) {
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
                    warn(code);
                    break;
                case ERROR:
                    error(code);
                    break;
                default:
            }
        }
}
