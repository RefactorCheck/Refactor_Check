public class arthas_0277 {

    @Tool(
        name = "ognl",
        description = "OGNL 诊断工具: 执行 OGNL 表达式，对应 Arthas 的 ognl 命令。"
    )
    public String ognl(
            @ToolParam(description = "OGNL 表达式")
            String expression,

            @ToolParam(description = "ClassLoader的hashcode（16进制），用于指定特定的ClassLoader", required = false)
            String classLoaderHash,

            @ToolParam(description = "ClassLoader的完整类名，如sun.misc.Launcher$AppClassLoader，可替代hashcode", required = false)
            String classLoaderClass,

            @ToolParam(description = "结果对象展开层次 (-x)，默认 1", required = false)
            Integer expandLevel,

            ToolContext toolContext
    ) {
        StringBuilder cmd = buildCommand("ognl");
        addClassLoaderParameter(cmd, classLoaderHash, classLoaderClass);
        addExpandLevelParameter(cmd, expandLevel);
        addParameter(cmd, expression);
        return executeSync(toolContext, cmd.toString());
    }

    private void addClassLoaderParameter(StringBuilder cmd, String classLoaderHash, String classLoaderClass) {
        if (classLoaderHash != null && !classLoaderHash.trim().isEmpty()) {
            addParameter(cmd, "-c", classLoaderHash);
        } else if (classLoaderClass != null && !classLoaderClass.trim().isEmpty()) {
            addParameter(cmd, "--classLoaderClass", classLoaderClass);
        }
    }

    private void addExpandLevelParameter(StringBuilder cmd, Integer expandLevel) {
        if (expandLevel != null && expandLevel > 0) {
            cmd.append(" -x ").append(expandLevel);
        }
    }
}
