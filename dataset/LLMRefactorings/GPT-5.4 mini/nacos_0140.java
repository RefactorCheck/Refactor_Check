public class nacos_0140 {

        public static void endRefactored(final String name, final Logger logger, final String level) {
            Map<String, Long> record = TIME_RECORD.get();
            long contextTime = System.currentTimeMillis() - record.remove(name);
            if (record.isEmpty()) {
                TIME_RECORD.remove();
            }
            switch (level) {
                case LoggerUtils.DEBUG:
                    LoggerUtils.printIfDebugEnabled(logger, "{} cost time : {} ms", name, contextTime);
                    break;
                case LoggerUtils.INFO:
                    LoggerUtils.printIfInfoEnabled(logger, "{} cost time : {} ms", name, contextTime);
                    break;
                case LoggerUtils.TRACE:
                    LoggerUtils.printIfTraceEnabled(logger, "{} cost time : {} ms", name, contextTime);
                    break;
                case LoggerUtils.ERROR:
                    LoggerUtils.printIfErrorEnabled(logger, "{} cost time : {} ms", name, contextTime);
                    break;
                case LoggerUtils.WARN:
                    LoggerUtils.printIfWarnEnabled(logger, "{} cost time : {} ms", name, contextTime);
                    break;
                default:
                    LoggerUtils.printIfErrorEnabled(logger, "level not found , {} cost time : {} ms",
                        name, contextTime);
                    break;
            }
        }
}
