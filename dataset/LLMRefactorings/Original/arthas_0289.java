public class arthas_0289 {

        private void startStreamableServer(McpServerProperties properties, ToolClassification classification) {
            // 初始化 SessionManager
            this.sessionManager = new ArthasCommandSessionManager(commandExecutor);
            logger.info("Initialized ArthasCommandSessionManager for MCP server");
    
            int maxSessions = com.taobao.arthas.mcp.server.task.TaskDefaults.DEFAULT_MAX_CONCURRENT_TASK_SESSIONS;
            this.taskExecutor = new ThreadPoolExecutor(
                    maxSessions,                       // corePoolSize: 与 session 上限一致
                    maxSessions,                       // maxPoolSize: 固定大小，避免动态扩缩
                    0L, TimeUnit.MILLISECONDS,
                    new SynchronousQueue<>(),           // 无缓冲：直接交付或拒绝，无隐式排队
                    new McpTaskThreadFactory(),         // 具名线程，便于 thread dump 诊断
                    new ThreadPoolExecutor.AbortPolicy() // 兜底：超出直接抛 RejectedExecutionException
            );
            logger.info("Created MCP task executor: fixedSize={}, queue=SynchronousQueue (no buffering)",
                    maxSessions);
            
            McpStreamableServerTransportProvider transportProvider = createStreamableHttpTransportProvider(properties);
            streamableHandler = transportProvider.getMcpRequestHandler();
            unifiedMcpHandler.setStreamableHandler(streamableHandler);
    
            // 准备任务感知工具列表（taskSupport = OPTIONAL 或 REQUIRED）
            List<ToolCallback> taskAwareTools = new ArrayList<>();
            taskAwareTools.addAll(classification.optionalTaskTools);
            taskAwareTools.addAll(classification.requiredTaskTools);
            
            boolean hasTaskTools = !taskAwareTools.isEmpty();
    
            McpServer.StreamableServerNettySpecification serverSpec = McpServer.netty(transportProvider)
                    .serverInfo(new Implementation(properties.getName(), properties.getVersion()))
                    .capabilities(buildServerCapabilities(properties, hasTaskTools))
                    .instructions(properties.getInstructions())
                    .requestTimeout(properties.getRequestTimeout())
                    .commandExecutor(commandExecutor)
                    .sessionManager(this.sessionManager)
                    .objectMapper(properties.getObjectMapper() != null ? properties.getObjectMapper() : JsonParser.getObjectMapper());
    
            // 只注册普通工具（taskSupport = FORBIDDEN）
            serverSpec.tools(McpToolUtils.toStreamableToolSpecifications(classification.normalTools));
            logger.debug("Registered {} normal tools", classification.normalTools.size());
            
            if (hasTaskTools) {
                configureTaskSupport(serverSpec, taskAwareTools);
            }
    
            streamableServer = serverSpec.build();
        }
}
