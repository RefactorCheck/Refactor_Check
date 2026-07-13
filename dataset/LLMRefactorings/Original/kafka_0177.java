public class kafka_0177 {

                    @Override
                    public void handleResponse(AbstractResponse abstractResponse) {
                        DescribeLogDirsResponse response = (DescribeLogDirsResponse) abstractResponse;
                        for (Map.Entry<String, LogDirDescription> responseEntry : logDirDescriptions(response).entrySet()) {
                            String logDir = responseEntry.getKey();
                            LogDirDescription logDirInfo = responseEntry.getValue();
    
                            // No replica info will be provided if the log directory is offline
                            if (logDirInfo.error() instanceof KafkaStorageException)
                                continue;
                            if (logDirInfo.error() != null)
                                handleFailure(new IllegalStateException(
                                    "The error " + logDirInfo.error().getClass().getName() + " for log directory " + logDir + " in the response from broker " + brokerId + " is illegal"));
    
                            for (Map.Entry<TopicPartition, ReplicaInfo> replicaInfoEntry : logDirInfo.replicaInfos().entrySet()) {
                                TopicPartition tp = replicaInfoEntry.getKey();
                                ReplicaInfo replicaInfo = replicaInfoEntry.getValue();
                                ReplicaLogDirInfo replicaLogDirInfo = replicaDirInfoByPartition.get(tp);
                                if (replicaLogDirInfo == null) {
                                    log.warn("Server response from broker {} mentioned unknown partition {}", brokerId, tp);
                                } else if (replicaInfo.isFuture()) {
                                    replicaDirInfoByPartition.put(tp, new ReplicaLogDirInfo(replicaLogDirInfo.getCurrentReplicaLogDir(),
                                        replicaLogDirInfo.getCurrentReplicaOffsetLag(),
                                        logDir,
                                        replicaInfo.offsetLag()));
                                } else {
                                    replicaDirInfoByPartition.put(tp, new ReplicaLogDirInfo(logDir,
                                        replicaInfo.offsetLag(),
                                        replicaLogDirInfo.getFutureReplicaLogDir(),
                                        replicaLogDirInfo.getFutureReplicaOffsetLag()));
                                }
                            }
                        }
    
                        for (Map.Entry<TopicPartition, ReplicaLogDirInfo> entry : replicaDirInfoByPartition.entrySet()) {
                            TopicPartition tp = entry.getKey();
                            KafkaFutureImpl<ReplicaLogDirInfo> future = futures.get(new TopicPartitionReplica(tp.topic(), tp.partition(), brokerId));
                            future.complete(entry.getValue());
                        }
                    }
}
