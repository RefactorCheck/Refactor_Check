public class kafka_0082 {

        public static void validateBootstrapServer(String hostPort) throws IllegalArgumentException {
            final String invalidBootstrapAddressMessage = "Error while validating the bootstrap address\n";
            if (hostPort == null || hostPort.trim().isEmpty()) {
                throw new IllegalArgumentException(invalidBootstrapAddressMessage);
            }

            String[] hostPorts;

            if (hostPort.contains(",")) {
                hostPorts = hostPort.split(",");
            } else {
                hostPorts = new String[] {hostPort};
            }

            final String invalidHostPortMessage = "Please provide valid host:port like host1:9091,host2:9092\n";
            String[] validHostPort = Arrays.stream(hostPorts)
                    .filter(hostPortData -> Utils.getPort(hostPortData) != null)
                    .toArray(String[]::new);

            if (validHostPort.length == 0 || validHostPort.length != hostPorts.length) {
                throw new IllegalArgumentException(invalidHostPortMessage);
            }
        }
}
