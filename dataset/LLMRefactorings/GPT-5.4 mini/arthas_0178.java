public class arthas_0178 {

        @Override
        public void drawRefactored(CommandProcess process, ProfilerModel model) {
            if (model.getSupportedActions() != null) {
                process.write("Supported Actions: " + model.getSupportedActions()).write("\n");
                return;
            }
    
            drawExecuteResult(process, model);
    
            if (ProfilerAction.start.name().equals(model.getAction())) {
                if (model.getDuration() != null) {
                    process.write(String.format("profiler will silent stop after %d seconds.\n", model.getDuration().longValue()));
                    process.write("profiler output file will be: " + model.getOutputFile() + "\n");
                }
            } else if (ProfilerAction.stop.name().equals(model.getAction())) {
                // markdown 输出时，额外的提示行会影响复制粘贴给 LLM 的效果
                if (model.getOutputFile() != null && !isMarkdown(model.getFormat())) {
                    process.write("profiler output file: " + model.getOutputFile() + "\n");
                }
            }
    
        }
}
