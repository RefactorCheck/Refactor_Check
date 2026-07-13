public class arthas_0077 {

        private ServerCapabilities buildServerCapabilitiesRefactored(McpServerProperties properties, boolean enableTasks) {
            ServerCapabilities.Builder builder = ServerCapabilities.builder()
                    .prompts(new ServerCapabilities.PromptCapabilities(properties.isPromptChangeNotification()))
                    .resources(new ServerCapabilities.ResourceCapabilities(properties.isResourceSubscribe(), properties.isResourceChangeNotification()))
                    .tools(new ServerCapabilities.ToolCapabilities(properties.isToolChangeNotification()));
            
            // 只有在有任务工具时才声明 tasks capability
            if (enableTasks) {
                // 声明服务器支持的任务能力
                ServerCapabilities.TaskCapabilities taskCapabilities = ServerCapabilities.TaskCapabilities.builder()
                        .list()        // 支持 tasks/list（列出所有任务）
                        .cancel()      // 支持 tasks/cancel（取消任务）
                        .toolsCall()   // 支持 tools/call 的任务增强执行（包括 tasks/get 和 tasks/result）
                        .build();
                
                builder.tasks(taskCapabilities);
                logger.info("Tasks capability enabled (supports list, cancel, tools/call with tasks)");
            } else {
                logger.info("Tasks capability disabled (no task-aware tools)");
            }
            
            return builder.build();
        }
}
