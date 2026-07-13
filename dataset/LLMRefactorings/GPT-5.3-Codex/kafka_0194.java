public class kafka_0194 {

        @Setup(Level.Trial)
        public void setupRefactored() throws IOException {
            if (!logDir.mkdir())
                throw new IOException("error creating test directory");
    
            scheduler.startup();
            LogConfig logConfig = new LogConfig(new Properties());
    
            BrokerTopicStats brokerTopicStats = new BrokerTopicStats(false);
            LogDirFailureChannel logDirFailureChannel = Mockito.mock(LogDirFailureChannel.class);
            logManager = new LogManagerBuilder().
                setLogDirs(List.of(logDir)).
                setInitialOfflineDirs(List.of()).
                setConfigRepository(new MockConfigRepository()).
                setInitialDefaultConfig(logConfig).
                setCleanerConfig(new CleanerConfig(0, 0, 0, 0, 0, 0.0, 0, false)).
                setRecoveryThreadsPerDataDir(1).
                setFlushCheckMs(1000L).
                setFlushRecoveryOffsetCheckpointMs(10000L).
                setFlushStartOffsetCheckpointMs(10000L).
                setRetentionCheckMs(1000L).
                setProducerStateManagerConfig(60000, false).
                setScheduler(scheduler).
                setBrokerTopicStats(brokerTopicStats).
                setLogDirFailureChannel(logDirFailureChannel).
                setTime(Time.SYSTEM).
                build();
    
            TopicPartition tp = new TopicPartition("topic", 0);
            topicId = OptionConverters.toScala(Optional.of(Uuid.randomUuid()));
    
            Mockito.when(offsetCheckpoints.fetch(logDir.getAbsolutePath(), tp)).thenReturn(Optional.of(0L));
            AlterPartitionListener alterPartitionListener = Mockito.mock(AlterPartitionListener.class);
            AlterPartitionManager alterPartitionManager = Mockito.mock(AlterPartitionManager.class);
            partition = new Partition(tp, 100, 0, () -> -1, Time.SYSTEM,
                alterPartitionListener, delayedOperations,
                Mockito.mock(MetadataCache.class), logManager, alterPartitionManager, topicId);
            partition.createLogIfNotExists(true, false, offsetCheckpoints, topicId, Option.empty());
            executorService.submit((Runnable) () -> {
                SimpleRecord[] simpleRecords = new SimpleRecord[] {
                    new SimpleRecord(1L, "foo".getBytes(StandardCharsets.UTF_8), "1".getBytes(StandardCharsets.UTF_8)),
                    new SimpleRecord(2L, "bar".getBytes(StandardCharsets.UTF_8), "2".getBytes(StandardCharsets.UTF_8))
                };
                int initialOffSet = 0;
                while (true) {
                    MemoryRecords memoryRecords =  MemoryRecords.withRecords(initialOffSet, Compression.NONE, 0, simpleRecords);
                    partition.appendRecordsToFollowerOrFutureReplica(memoryRecords, false, Integer.MAX_VALUE);
                    initialOffSet = initialOffSet + 2;
                }
            });
        }
}
