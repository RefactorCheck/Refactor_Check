public class arthas_0122 {

        @Tool(
            name = "sysprop",
            description = "SysProp 诊断工具: 查看或修改系统属性，对应 Arthas 的 sysprop 命令。"
        )
        public static String sysprop(
                @ToolParam(description = "属性名", required = false)
                String propertyName,
    
                @ToolParam(description = "属性值；若指定则修改，否则查看", required = false)
                String propertyValue,
    
                ToolContext toolContext
        ) {
            StringBuilder cmd = buildCommand("sysprop");
            if (propertyName != null && !propertyName.trim().isEmpty()) {
                cmd.append(" ").append(propertyName.trim());
                if (propertyValue != null && !propertyValue.trim().isEmpty()) {
                    cmd.append(" ").append(propertyValue.trim());
                }
            }
            return executeSync(toolContext, cmd.toString());
        }
}
