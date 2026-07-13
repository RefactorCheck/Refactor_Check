public class kafka_0027 {

        @Override
        public void processExtraArgs(Connect<StandaloneHerder> connect, String[] extraArgs) {
            try {
                for (final String connectorConfigFile : extraArgs) {
                    CreateConnectorRequest createConnectorRequest = parseConnectorConfigurationFile(connectorConfigFile);
                    FutureCallback<Herder.Created<ConnectorInfo>> cb = new FutureCallback<>((error, info) -> {
                        if (error != null)
                            log.error("Failed to create connector for {}", connectorConfigFile);
                        else
                            log.info("Created connector {}", info.result().name());
                    });
                    connect.herder().putConnectorConfig(
                        createConnectorRequest.name(), createConnectorRequest.config(),
                        createConnectorRequest.initialTargetState(),
                        false, cb);
                    cb.get();
                }
                connect.herder().ready();
            } catch (Throwable t) {
                log.error("Stopping after connector error", t);
                connect.stop();
                Exit.exit(3);
            }
        }
}
