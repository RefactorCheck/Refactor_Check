public class arthas_0197 {

        @Override
        public void process(CommandProcess process, final boolean useCache) {
            boolean cacheEnabled = useCache;
            process.interruptHandler(new ClassLoaderMetaspaceInterruptHandler(this));
    
            long durationMillis;
            long periodMillis;
            try {
                durationMillis = parseTimeMillis(duration, "duration");
                periodMillis = parseTimeMillis(period, "period");
            } catch (IllegalArgumentException e) {
                process.end(-1, e.getMessage());
                return;
            }
            if (limit != null && limit <= 0) {
                process.end(-1, "limit must be greater than 0.");
                return;
            }
    
            try {
                logger.debug("{} command start, durationMillis={}, periodMillis={}, hashFilter={}, classLoaderClassFilter={}, limit={}, verbose={}",
                        DIAG_LOG_PREFIX, durationMillis, periodMillis, hashCode, classLoaderClass, limit, verbose);
                List<Row> rows = collect(process.session().getInstrumentation(), durationMillis, periodMillis);
                rows = sortAndLimit(rows, limit);
                logger.debug("{} command finish, rowsAfterLimit={}", DIAG_LOG_PREFIX, rows.size());
                RowAffect affect = new RowAffect();
                affect.rCnt(rows.size());
                process.appendResult(new ClassLoaderMetaspaceModel()
                        .setRows(rows)
                        .setDurationMillis(durationMillis)
                        .setPeriodMillis(periodMillis)
                        .setVerbose(verbose));
                process.appendResult(new RowAffectModel(affect));
                process.end();
            } catch (InterruptedException e) {
                process.end(-1, "Processing has been interrupted");
            } catch (Throwable e) {
                logger.warn("classloader-metaspace failed", e);
                process.end(-1, "classloader-metaspace failed: " + e.getMessage());
            }
        }
}
