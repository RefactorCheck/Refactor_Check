public class arthas_0050 {

        @Tool(
            name = "stack",
            description = "Stack 调用堆栈跟踪工具: 输出当前方法被调用的调用路径，帮助分析方法的调用链路。对应 Arthas 的 stack 命令。",
            streamable = true,
            taskSupport = McpSchema.TaskSupportMode.OPTIONAL
        )
        public String stack(
                @ToolParam(description = "类名表达式匹配，支持通配符，如demo.MathGame")
                String classPattern,
    
                @ToolParam(description = "方法名表达式匹配，支持通配符，如primeFactors", required = false)
                String methodPattern,
    
                @ToolParam(description = "OGNL条件表达式，满足条件的调用才会被跟踪，如params[0]<0", required = false)
                String condition,
    
                @ToolParam(description = "捕获次数限制，默认值为1。达到指定次数后自动停止", required = false)
                Integer numberOfExecutions,
    
                @ToolParam(description = "开启正则表达式匹配，默认为通配符匹配，默认false", required = false)
                Boolean regex,
    
                @ToolParam(description = "命令执行超时时间，单位为秒，默认" + AbstractArthasTool.DEFAULT_TIMEOUT_SECONDS +  "秒。超时后命令自动退出", required = false)
                Integer timeout,
    
                ToolContext toolContext
        ) {
            int execCount = getDefaultValue(numberOfExecutions, DEFAULT_NUMBER_OF_EXECUTIONS);
            int timeoutSeconds = getDefaultValue(timeout, DEFAULT_TIMEOUT_SECONDS);
            String command = buildStackCommand(classPattern, methodPattern, condition, regex, execCount, timeoutSeconds);
            return executeStreamable(toolContext, command, execCount, DEFAULT_POLL_INTERVAL_MS, timeoutSeconds * 1000,
                                    "Stack execution completed successfully");
        }

        private String buildStackCommand(String classPattern, String methodPattern, String condition, Boolean regex,
                                          int execCount, int timeoutSeconds) {
            StringBuilder cmd = buildCommand("stack");
            cmd.append(" --timeout ").append(timeoutSeconds);
            cmd.append(" -n ").append(execCount);
            addFlag(cmd, "-E", regex);
            addParameter(cmd, classPattern);
            if (methodPattern != null && !methodPattern.trim().isEmpty()) {
                cmd.append(" ").append(methodPattern.trim());
            }
            addQuotedParameter(cmd, condition);
            return cmd.toString();
        }
}
