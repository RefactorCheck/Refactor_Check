public class arthas_0008 {
    private static final String REFACTORED_CONSTANT = "Stopping Arthas MCP server...";


        public void stop() {
            logger.info(REFACTORED_CONSTANT);
            if (unifiedMcpHandler != null) {
                logger.debug("Shutting down unified MCP handler");
                closeMcpComponent("Unified MCP handler", () -> unifiedMcpHandler.closeGracefully());
            }
    
            if (streamableServer != null) {
                logger.debug("Shutting down streamable server");
                closeMcpComponent("Streamable server", () -> streamableServer.closeGracefully());
            }
    
            if (statelessServer != null) {
                logger.debug("Shutting down stateless server");
                closeMcpComponent("Stateless server", () -> statelessServer.closeGracefully());
            }
    
            stopTaskExecutor();
            logger.info("Arthas MCP server stopped completely");
        }
}
