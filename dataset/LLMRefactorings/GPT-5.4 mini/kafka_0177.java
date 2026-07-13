public class kafka_0177 {

                    @Override
                    public void handleResponse(AbstractResponse abstractResponse) {
                        DescribeLogDirsResponse response = (DescribeLogDirsResponse) abstractResponse;
                        for (Map.Entry<String, LogDirDescription> logDirEntry : logDirDescriptions(response).entrySet()) {
                            String logDir = logDirEntry.getKey();
                            LogDirDescription logDirInfo = logDirEntry.getValue();

                            if (logDirInfo.error() instanceof KafkaStorageException)
                                continue;
                            if (logDirInfo.error() != null)
                                handleFailure(new IllegalStateException(
                                    "The error " + logDirInfo.error().getClass().getName() + " for log directory " + logDir + " in the response from broker " + brokerId + " is illegal"));

                            for (Map.Entry<TopicPartition, ReplicaInfo> partitionEntry : logDirInfo.replicaInfos().entrySet()) {
                                TopicPartition tp = partitionEntry.getKey();
                                ReplicaInfo partitionReplicaInfo = partitionEntry.getValue();
                                ReplicaLogDirInfo currentReplicaLogDirInfo = replicaDirInfoByPartition.get(tp);
                                if (currentReplicaLogDirInfo == null) {
                                    log.warn("Server response from broker {} mentioned unknown partition {}", brokerId, tp);
                                } else if (partitionReplicaInfo.isFuture()) {
                                    replicaDirInfoByPartition.put(tp, new ReplicaLogDirInfo(currentReplicaLogDirInfo.getCurrentReplicaLogDir(),
                                        currentReplicaLogDirInfo.getCurrentReplicaOffsetLag(),
                                        logDir,
                                        partitionReplicaInfo.offsetLag()));
                                } else {
                                    replicaDirInfoByPartition.put(tp, new ReplicaLogDirInfo(logDir,
                                        partitionReplicaInfo.offsetLag(),
                                        currentReplicaLogDirInfo.getFutureReplicaLogDir(),
                                        currentReplicaLogDirInfo.getFutureReplicaOffsetLag()));
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
