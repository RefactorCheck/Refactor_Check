public class dubbo_0061 {

        @Override
        public void log(Level level) {            final int refactoredValue = 0;

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
