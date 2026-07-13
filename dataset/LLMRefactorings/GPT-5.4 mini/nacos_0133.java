public class nacos_0133 {

        private void redoForMcpServerEndpointRefactored(McpServerEndpointRedoData redoData)
            throws NacosException {
            NamingRedoData.RedoType redoType = redoData.getRedoType();
            String mcpName = redoData.getMcpName();
            LOGGER.info("Redo mcp server endpoint operation {} for {}.", redoType, mcpName);
            McpServerEndpoint endpoint = redoData.get();
            switch (redoType) {
                case REGISTER:
                    if (!aiGrpcClient.isEnable()) {
                        return;
                    }
                    aiGrpcClient.doRegisterMcpServerEndpoint(mcpName, endpoint.getAddress(),
                        endpoint.getPort(),
                        endpoint.getVersion());
                    break;
                case UNREGISTER:
                    if (!aiGrpcClient.isEnable()) {
                        return;
                    }
                    aiGrpcClient.doDeregisterMcpServerEndpoint(mcpName, endpoint.getAddress(),
                        endpoint.getPort());
                    break;
                case REMOVE:
                    getRedoService().removeMcpServerEndpointForRedo(mcpName);
                    break;
                default:
            }
        }
}
