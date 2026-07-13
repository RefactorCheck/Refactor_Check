public class arthas_0227 {

    private CompletableFuture<McpSchema.InitializeResult> initializeRequestHandler(
            McpSchema.InitializeRequest initializeRequest) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Client initialize request - Protocol: {}, Capabilities: {}, Info: {}",
                    initializeRequest.getProtocolVersion(), initializeRequest.getCapabilities(),
                    initializeRequest.getClientInfo());

            String serverProtocolVersion = resolveServerProtocolVersion(initializeRequest);

            return new McpSchema.InitializeResult(serverProtocolVersion, serverCapabilities, serverInfo, instructions);
        });
    }

    private String resolveServerProtocolVersion(McpSchema.InitializeRequest initializeRequest) {
        String serverProtocolVersion = protocolVersions.get(protocolVersions.size() - 1);

        if (protocolVersions.contains(initializeRequest.getProtocolVersion())) {
            serverProtocolVersion = initializeRequest.getProtocolVersion();
        } else {
            logger.warn(
                    "Client requested unsupported protocol version: {}, " + "so the server will suggest {} instead",
                    initializeRequest.getProtocolVersion(), serverProtocolVersion);
        }
        return serverProtocolVersion;
    }
}
