public class keycloak_0048 {

        public static DestinationValidator forProtocolMap(String[] protocolMappings) {
            if (protocolMappings == null) {
                protocolMappings = DEFAULT_PROTOCOL_TO_PORT_MAP;
            }
    
            Map<String, Integer> knownPorts = new HashMap<>();
            Map<Integer, String> knownProtocols = new HashMap<>();
    
            populateProtocolMaps(protocolMappings, knownPorts, knownProtocols);
    
            return new DestinationValidator(knownPorts, knownProtocols);
        }
    
        private static void populateProtocolMaps(String[] protocolMappings, Map<String, Integer> knownPorts, Map<Integer, String> knownProtocols) {
            for (String protocolMapping : protocolMappings) {
                Matcher m = PROTOCOL_MAP_PATTERN.matcher(protocolMapping);
                if (m.matches()) {
                    Integer port = Integer.valueOf(m.group(2));
                    String proto = m.group(1);
    
                    knownPorts.put(proto, port);
                    knownProtocols.put(port, proto);
                }
            }
        }
}
