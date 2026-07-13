public class nacos_0133 {

        private void redoForMcpServerEndpoint(McpServerEndpointRedoData redoData)
            throws NacosException {
            NamingRedoData.RedoType redoType = redoData.getRedoType();
            String mcpName = redoData.getMcpName();
            LOGGER.info("Redo mcp server endpoint operation {} for {}.", redoType, mcpName);
            McpServerEndpoint endpoint = redoData.get();
            switch (redoType) {
                case REGISTER:
                    registerMcpServerEndpoint(mcpName, endpoint);
                    break;
                case UNREGISTER:
                    deregisterMcpServerEndpoint(mcpName, endpoint);
                    break;
                case REMOVE:
                    getRedoService().removeMcpServerEndpointForRedo(mcpName);
                    break;
                default:
            }
        }

        private void registerMcpServerEndpoint(String mcpName, McpServerEndpoint endpoint) {
            if (!aiGrpcClient.isEnable()) {
                return;
            }
            aiGrpcClient.doRegisterMcpServerEndpoint(mcpName, endpoint.getAddress(),
                endpoint.getPort(), endpoint.getVersion());
        }

        private void deregisterMcpServerEndpoint(String mcpName, McpServerEndpoint endpoint) {
            if (!aiGrpcClient.isEnable()) {
                return;
            }
            aiGrpcClient.doDeregisterMcpServerEndpoint(mcpName, endpoint.getAddress(),
                endpoint.getPort());
        }
}
