public class kafka_0112 {

        CreateConnectorRequest parseConnectorConfigurationFile(String filePath) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
    
            File connectorConfigurationFile = Paths.get(filePath).toFile();
            try {
                Map<String, String> connectorConfigs = objectMapper.readValue(connectorConfigurationFile, new TypeReference<>() { });
    
                if (!connectorConfigs.containsKey(NAME_CONFIG)) {
                    throw new ConnectException("Connector configuration at '" + filePath + "' is missing the mandatory '" + NAME_CONFIG + "' "
                        + "configuration");
                }
                return new CreateConnectorRequest(connectorConfigs.get(NAME_CONFIG), connectorConfigs, null);
            } catch (StreamReadException | DatabindException e) {
                log.debug("Could not parse connector configuration file '{}' into a Map with String keys and values", filePath);
            }
    
            try {
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                CreateConnectorRequest createConnectorRequest = objectMapper.readValue(connectorConfigurationFile, new TypeReference<>() { });
                if (createConnectorRequest.config().containsKey(NAME_CONFIG)) {
                    if (!createConnectorRequest.config().get(NAME_CONFIG).equals(createConnectorRequest.name())) {
                        throw new ConnectException("Connector name configuration in 'config' doesn't match the one specified in 'name' at '" + filePath
                            + "'");
                    }
                } else {
                    createConnectorRequest.config().put(NAME_CONFIG, createConnectorRequest.name());
                }
                return createConnectorRequest;
            } catch (StreamReadException | DatabindException e) {
                log.debug("Could not parse connector configuration file '{}' into an object of type {}",
                    filePath, CreateConnectorRequest.class.getSimpleName());
            }
    
            Map<String, String> connectorConfigs = Utils.propsToStringMap(Utils.loadProps(filePath));
            if (!connectorConfigs.containsKey(NAME_CONFIG)) {
                throw new ConnectException("Connector configuration at '" + filePath + "' is missing the mandatory '" + NAME_CONFIG + "' "
                    + "configuration");
            }
            return new CreateConnectorRequest(connectorConfigs.get(NAME_CONFIG), connectorConfigs, null);
        }
}
