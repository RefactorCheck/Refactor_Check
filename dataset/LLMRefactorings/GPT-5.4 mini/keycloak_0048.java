public class keycloak_0048 {

        public static DestinationValidator forProtocolMap(String[] protocolMappings) {
            if (protocolMappings == null) {
                protocolMappings = DEFAULT_PROTOCOL_TO_PORT_MAP;
            }
    
            Map<String, Integer> protocolToPort = new HashMap<>();
            Map<Integer, String> portToProtocol = new HashMap<>();
    
            for (String protocolMapping : protocolMappings) {
                Matcher m = PROTOCOL_MAP_PATTERN.matcher(protocolMapping);
                if (m.matches()) {
                    Integer port = Integer.valueOf(m.group(2));
                    String proto = m.group(1);
    
                    protocolToPort.put(proto, port);
                    portToProtocol.put(port, proto);
                }
            }
    
            return new DestinationValidator(protocolToPort, portToProtocol);
        }
}
