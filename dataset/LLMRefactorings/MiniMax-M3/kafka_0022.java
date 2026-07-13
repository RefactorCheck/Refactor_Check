public class kafka_0022 {

        private void addHerder(SourceAndTarget sourceAndTarget) {
            log.info("creating herder for {}", sourceAndTarget.toString());
            Map<String, String> workerProps = config.workerConfig(sourceAndTarget);
            List<String> restNamespace = createRestNamespace(sourceAndTarget);
            String workerId = generateWorkerId(sourceAndTarget);
            Plugins plugins = new Plugins(workerProps);
            plugins.compareAndSwapWithDelegatingLoader();
            // create DistributedConfig only after compareAndSwapWithDelegatingLoader to
            // ensure plugin classes on plugin.path are loadable
            DistributedConfig distributedConfig = new DistributedConfig(workerProps);
            String kafkaClusterId = distributedConfig.kafkaClusterId();
            String clientIdBase = ConnectUtils.clientIdBase(distributedConfig);
            // Create the admin client to be shared by all backing stores for this herder
            SharedTopicAdmin sharedAdmin = createSharedAdmin(distributedConfig, kafkaClusterId, clientIdBase);
            KafkaOffsetBackingStore offsetBackingStore = createOffsetBackingStore(sharedAdmin, clientIdBase, plugins);
            offsetBackingStore.configure(distributedConfig);
            ConnectorClientConfigOverridePolicy clientConfigOverridePolicy = new AllConnectorClientConfigOverridePolicy();
            clientConfigOverridePolicy.configure(config.originals());
            Worker worker = new Worker(workerId, time, plugins, distributedConfig, offsetBackingStore, clientConfigOverridePolicy);
            WorkerConfigTransformer configTransformer = worker.configTransformer();
            Converter internalValueConverter = worker.getInternalValueConverter();
            StatusBackingStore statusBackingStore = new KafkaStatusBackingStore(time, internalValueConverter, sharedAdmin, clientIdBase);
            statusBackingStore.configure(distributedConfig);
            ConfigBackingStore configBackingStore = new KafkaConfigBackingStore(
                    internalValueConverter,
                    distributedConfig,
                    configTransformer,
                    sharedAdmin,
                    clientIdBase);
            // Pass the shared admin to the distributed herder as an additional AutoCloseable object that should be closed when the
            // herder is stopped. MirrorMaker has multiple herders, and having the herder own the close responsibility is much easier than
            // tracking the various shared admin objects in this class.
            Herder herder = new MirrorHerder(config, sourceAndTarget, distributedConfig, time, worker,
                    kafkaClusterId, statusBackingStore, configBackingStore,
                    advertisedUrl.toString(), restClient, clientConfigOverridePolicy,
                    restNamespace, sharedAdmin);
            herders.put(sourceAndTarget, herder);
        }

        private List<String> createRestNamespace(SourceAndTarget sourceAndTarget) {
            String encodedSource = encodePath(sourceAndTarget.source());
            String encodedTarget = encodePath(sourceAndTarget.target());
            return List.of(encodedSource, encodedTarget);
        }

        private SharedTopicAdmin createSharedAdmin(DistributedConfig distributedConfig, String kafkaClusterId, String clientIdBase) {
            Map<String, Object> adminProps = new HashMap<>(distributedConfig.originals());
            adminProps.put(CLIENT_ID_CONFIG, clientIdBase + "shared-admin");
            ConnectUtils.addMetricsContextProperties(adminProps, distributedConfig, kafkaClusterId);
            return new SharedTopicAdmin(adminProps);
        }

        private KafkaOffsetBackingStore createOffsetBackingStore(SharedTopicAdmin sharedAdmin, String clientIdBase, Plugins plugins) {
            return new KafkaOffsetBackingStore(sharedAdmin, () -> clientIdBase,
                    plugins.newInternalConverter(true, JsonConverter.class.getName(),
                            Map.of(JsonConverterConfig.SCHEMAS_ENABLE_CONFIG, "false")));
        }
}
