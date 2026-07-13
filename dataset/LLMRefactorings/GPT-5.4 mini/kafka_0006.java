public class kafka_0006 {

        public Connect<H> startWorker(Map<String, String> workerProps) {
            log.info("Kafka Connect worker initializing ...");
            long initStart = time.hiResClockMs();

            WorkerInfo initInfo = new WorkerInfo();
            initInfo.logAll();

            log.info("Scanning for plugin classes. This might take a moment ...");
            Plugins plugins = new Plugins(workerProps);
            plugins.compareAndSwapWithDelegatingLoader();

            // must call createConfig after plugins.compareAndSwapWithDelegatingLoader()
            // because WorkerConfig may instantiate classes only available on plugin.path.
            T config = createConfig(workerProps);
            log.debug("Kafka cluster ID: {}", config.kafkaClusterId());

            RestClient restClient = new RestClient(config);

            ConnectRestServer restServer = new ConnectRestServer(config.rebalanceTimeout(), restClient, config.originals());
            restServer.initializeServer();

            URI advertisedUrl = restServer.advertisedUrl();
            String workerId = advertisedUrl.getHost() + ":" + advertisedUrl.getPort();

            ConnectorClientConfigOverridePolicy connectorClientConfigOverridePolicy = plugins.newPlugin(
                    config.getString(WorkerConfig.CONNECTOR_CLIENT_POLICY_CLASS_CONFIG),
                    config, ConnectorClientConfigOverridePolicy.class);

            H herder = createHerder(config, workerId, plugins, connectorClientConfigOverridePolicy, restServer, restClient);

            final Connect<H> connect = new Connect<>(herder, restServer);
            log.info("Kafka Connect worker initialization took {}ms", time.hiResClockMs() - initStart);
            try {
                connect.start();
            } catch (Exception e) {
                log.error("Failed to start Connect", e);
                connect.stop();
                Exit.exit(3);
            }

            return connect;
        }
}
