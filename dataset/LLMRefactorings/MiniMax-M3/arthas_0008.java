public class arthas_0008 {

        public void stop() {
            logger.info("Stopping Arthas MCP server...");
            shutdownIfPresent("Unified MCP handler", unifiedMcpHandler);
            shutdownIfPresent("Streamable server", streamableServer);
            shutdownIfPresent("Stateless server", statelessServer);
            stopTaskExecutor();
            logger.info("Arthas MCP server stopped completely");
        }

        private void shutdownIfPresent(String name, McpComponent component) {
            if (component != null) {
                logger.debug("Shutting down " + name.toLowerCase());
                closeMcpComponent(name, () -> component.closeGracefully());
            }
        }
}
