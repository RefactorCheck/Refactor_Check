public class arthas_0292 {

        private List<Row> collect(Instrumentation instrumentation, long durationMillis, long periodMillis)
                throws Exception {
            Path output = Files.createTempFile("arthas-classloader-metaspace-", ".jfr");
            Recording recording = null;
            long startNanos = System.nanoTime();
            try {
                recording = new Recording();
                recording.enable(STATS_EVENT_NAME)
                        .withPeriod(Duration.ofMillis(periodMillis))
                        .withoutStackTrace();
                recording.enable(MappingEvent.class).withoutStackTrace();
    
                recording.start();
                MappingSummary mappingSummary = emitMappings(instrumentation);
                logMappingSummary(mappingSummary);
                waitForSample(durationMillis);
                recording.stop();
                recording.dump(output);
                long recordingSize = safeFileSize(output);
                RecordingData recordingData = readRecording(output);
                logRecordingSummary(recordingData, recordingSize);
                BuildResult buildResult = buildRows(recordingData);
                logBuildResult(buildResult);
                return buildResult.rows;
            } finally {
                cleanupRecording(output, recording, startNanos);
            }
        }

        private void cleanupRecording(Path output, Recording recording, long startNanos) {
            logger.debug("{} collection finish, costMillis={}", DIAG_LOG_PREFIX,
                    (System.nanoTime() - startNanos) / 1000000L);
            if (recording != null) {
                try {
                    recording.close();
                } catch (Throwable e) {
                    logger.debug("close JFR recording failed", e);
                }
            }
            try {
                Files.deleteIfExists(output);
            } catch (IOException e) {
                logger.debug("delete temp JFR file failed: {}", output, e);
            }
        }
}
