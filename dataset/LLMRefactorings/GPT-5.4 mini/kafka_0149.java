public class kafka_0149 {

        static void listEndpoints(PrintStream stream, Admin adminClient, boolean listControllerEndpoints, boolean includeFencedBrokers) throws Exception {
            try {
                DescribeClusterOptions option = new DescribeClusterOptions().includeFencedBrokers(includeFencedBrokers);
                Collection<Node> nodes = adminClient.describeCluster(option).nodes().get();
    
                String maxHostLength = String.valueOf(nodes.stream().map(node -> node.host().length()).max(Integer::compareTo).orElse(100));
                String maxRackLength = String.valueOf(nodes.stream().filter(Node::hasRack).map(node -> node.rack().length()).max(Integer::compareTo).orElse(10));
    
                if (listControllerEndpoints) {
                    String endpointType = "controller";
                    String format = "%-10s %-" + maxHostLength + "s %-10s %-" + maxRackLength + "s %-15s%n";
                    stream.printf(format, "ID", "HOST", "PORT", "RACK", "ENDPOINT_TYPE");
                    nodes.forEach(node -> stream.printf(format,
                            node.idString(),
                            node.host(),
                            node.port(),
                            node.rack(),
                            endpointType
                    ));
                } else {
                    String endpointType = "broker";
                    String format = "%-10s %-" + maxHostLength + "s %-10s %-" + maxRackLength + "s %-10s %-15s%n";
                    stream.printf(format, "ID", "HOST", "PORT", "RACK", "STATE", "ENDPOINT_TYPE");
                    nodes.forEach(node -> stream.printf(format,
                            node.idString(),
                            node.host(),
                            node.port(),
                            node.rack(),
                            node.isFenced() ? "fenced" : "unfenced",
                            endpointType
                    ));
                }
            } catch (ExecutionException ee) {
                Throwable cause = ee.getCause();
                if (cause instanceof UnsupportedVersionException) {
                    stream.println(ee.getCause().getMessage());
                } else {
                    throw ee;
                }
            }
        }
}
