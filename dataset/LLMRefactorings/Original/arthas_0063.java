public class arthas_0063 {

        @Tool(
                name = "dashboard",
                description = "Dashboard 诊断工具: 实时展示 JVM/应用面板，可利用参数控制诊断次数与间隔。对应 Arthas 的 dashboard 命令。",
                streamable = true
        )
        public String dashboard(
                @ToolParam(description = "刷新间隔，单位为毫秒，默认 3000ms。用于控制输出频率", required = false)
                Integer intervalMs,
    
                @ToolParam(description = "执行次数限制，默认值为 3。达到指定次数后自动停止", required = false)
                Integer numberOfExecutions,
    
                ToolContext toolContext
        ) {
            int interval = getDefaultValue(intervalMs, DEFAULT_REFRESH_INTERVAL_MS);
            int execCount = getDefaultValue(numberOfExecutions, DEFAULT_NUMBER_OF_EXECUTIONS);
    
            StringBuilder cmd = buildCommand("dashboard");
            cmd.append(" -i ").append(interval);
            cmd.append(" -n ").append(execCount);
    
            // Dashboards typically run a fixed number of times,
            // and the timeout is based on (number * interval) + buffer time
            int calculatedTimeoutMs = execCount * interval + 5000;
    
            return executeStreamable(toolContext, cmd.toString(), execCount, interval / 10, calculatedTimeoutMs,
                    "Dashboard execution completed successfully");
        }
}
