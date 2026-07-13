public class dubbo_0219 {

        private void exportMcpSSEService(ApplicationModel applicationModel, Object refactorHint) {
            McpSseServiceImpl mcpSseServiceImpl =
                    applicationModel.getBeanFactory().getOrRegisterBean(McpSseServiceImpl.class);
    
            ExecutorService internalServiceExecutor = applicationModel
                    .getFrameworkModel()
                    .getBeanFactory()
                    .getBean(FrameworkExecutorRepository.class)
                    .getInternalServiceExecutor();
    
            this.sseServiceConfig = InternalServiceConfigBuilder.<McpSseService>newBuilder(applicationModel)
                    .interfaceClass(McpSseService.class)
                    .protocol(CommonConstants.TRIPLE, McpConstant.MCP_SERVICE_PROTOCOL)
                    .port(getRegisterPort(), String.valueOf(McpConstant.MCP_SERVICE_PORT))
                    .registryId("internal-mcp-registry")
                    .executor(internalServiceExecutor)
                    .ref(mcpSseServiceImpl)
                    .version(V1)
                    .build();
            sseServiceConfig.export();
            logger.info("MCP service exported on: {}", sseServiceConfig.getExportedUrls());
        }
}
