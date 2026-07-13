public class dubbo_0197 {

        private void dumpJStack() {
            long now = System.currentTimeMillis();
    
            // dump every 10 minutes
            if (now - lastPrintTime < TEN_MINUTES_MILLS) {
                return;
            }
    
            if (!guard.tryAcquire()) {
                return;
            }
            ExecutorService pool = null;
            try {
                // To avoid multiple dump, check again
                if (System.currentTimeMillis() - lastPrintTime < TEN_MINUTES_MILLS) {
                    return;
                }
                pool = Executors.newSingleThreadExecutor();
                pool.execute(() -> {
                    String dumpPath = getDumpPath();
                    String dateStr = getDateString();
                    // try-with-resources
                    try (FileOutputStream jStackStream =
                            new FileOutputStream(new File(dumpPath, "Dubbo_JStack.log" + "." + dateStr))) {
                        jstack(jStackStream);
                    } catch (Exception t) {
                        logger.error(COMMON_UNEXPECTED_CREATE_DUMP, "", "", "dump jStack error", t);
                    }
                });
                lastPrintTime = System.currentTimeMillis();
            } finally {
                guard.release();
                // must shut down thread pool ,if not will lead to OOM
                if (pool != null) {
                    pool.shutdown();
                }
            }
        }
    
        private String getDateString() {
            SimpleDateFormat sdf;
            String os = SystemPropertyConfigUtils.getSystemProperty(SYSTEM_OS_NAME)
                    .toLowerCase();
            // window system don't support ":" in file name
            if (os.contains(OS_WIN_PREFIX)) {
                sdf = new SimpleDateFormat(WIN_DATETIME_FORMAT);
            } else {
                sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
            }
            return sdf.format(new Date());
        }
}
