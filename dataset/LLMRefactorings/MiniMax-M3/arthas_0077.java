public class arthas_0077 {

        private ServerCapabilities buildServerCapabilities(McpServerProperties properties, boolean enableTasks) {
            ServerCapabilities.Builder builder = ServerCapabilities.builder()
                    .prompts(new ServerCapabilities.PromptCapabilities(properties.isPromptChangeNotification()))
                    .resources(new ServerCapabilities.ResourceCapabilities(properties.isResourceSubscribe(), properties.isResourceChangeNotification()))
                    .tools(new ServerCapabilities.ToolCapabilities(properties.isToolChangeNotification()));
            
            if (enableTasks) {
                builder.tasks(buildTaskCapabilities());
                logger.info("Tasks capability enabled (supports list, cancel, tools/call with tasks)");
            } else {
                logger.info("Tasks capability disabled (no task-aware tools)");
            }
            
            return builder.build();
        }

        private ServerCapabilities.TaskCapabilities buildTaskCapabilities() {
            return ServerCapabilities.TaskCapabilities.builder()
                    .list()
                    .cancel()
                    .toolsCall()
                    .build();
        }
}
