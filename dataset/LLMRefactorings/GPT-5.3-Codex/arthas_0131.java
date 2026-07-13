public class arthas_0131 {

        @Override
        public void draw(CommandProcess process, LineModel model) {
            ObjectVO objectVO = model.getValue();
            String result = StringUtils.objectToString(
                    objectVO.needExpand() ? new ObjectView((ObjectView.normalizeMaxObjectLength(model.getSizeLimit())), objectVO).draw() : objectVO.getObject());
    
            StringBuilder sb = new StringBuilder();
            sb.append("ts=").append(DateUtils.formatDateTime(model.getTs()))
                    .append("; [thread=").append(model.getThreadName())
                    .append("(").append(model.getThreadId()).append(")")
                    .append(" cost=").append(model.getCost()).append("ms] ")
                    .append(model.getClassName()).append(".").append(model.getMethodName())
                    .append(model.getMethodDesc()).append(":").append(model.getLineNumber()).append("\n");
            sb.append("result=").append(result).append("\n");
            StackTraceElement[] stackTrace = model.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                sb.append("stack=\n");
                for (StackTraceElement stackTraceElement : stackTrace) {
                    sb.append("    at ").append(stackTraceElement.getClassName()).append(".")
                            .append(stackTraceElement.getMethodName()).append("(")
                            .append(stackTraceElement.getFileName()).append(":")
                            .append(stackTraceElement.getLineNumber()).append(")\n");
                }
            }
    
            process.write(sb.toString());
        }
}
