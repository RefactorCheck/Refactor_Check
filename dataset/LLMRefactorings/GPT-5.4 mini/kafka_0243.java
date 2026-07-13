public class kafka_0243 {

        void incrementalAlterConfigs(Map<String, Config> topicConfigs) throws ExecutionException, InterruptedException {
            Map<ConfigResource, Collection<AlterConfigOp>> configOps = new HashMap<>();
            for (Map.Entry<String, Config> topicConfig : topicConfigs.entrySet()) {
                Collection<AlterConfigOp> ops = new ArrayList<>();
                ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicConfig.getKey());
                for (ConfigEntry config : topicConfig.getValue().entries()) {
                    if (config.isDefault() && !shouldReplicateSourceDefault(config.name())) {
                        ops.add(new AlterConfigOp(config, AlterConfigOp.OpType.DELETE));
                    } else {
                        ops.add(new AlterConfigOp(config, AlterConfigOp.OpType.SET));
                    }
                }
                configOps.put(configResource, ops);
            }
            String targetCluster = sourceAndTarget.target();
            log.trace("Syncing configs for {} topics.", configOps.size());
            adminCall(() -> {
                targetAdminClient.incrementalAlterConfigs(configOps).values()
                    .forEach((k, v) -> v.whenComplete((x, e) -> {
                        if (e instanceof UnsupportedVersionException) {
                            log.error("Failed to sync configs for topic {} on cluster {} with " +
                                    "IncrementalAlterConfigs API", k.name(), targetCluster, e);
                            context.raiseError(new ConnectException("the target cluster '"
                                    + targetCluster + "' is not compatible with " +
                                    "IncrementalAlterConfigs " +
                                    "API", e));
                        } else if (e != null) {
                            log.warn("Could not alter configuration of topic {}.", k.name(), e);
                        } else {
                            log.debug("Successfully altered configuration of topic {}.", k.name());
                        }
                    }));
                return null;
            },
                () -> String.format("incremental alter topic configs %s on %s cluster", topicConfigs,
                        config.targetClusterAlias()));
        }
}
