public class arthas_0118 {

        private void renderNode(StringBuilder sb, TraceNode node, Ansi highlighted) {
            if (isPrintCost && node instanceof MethodNode) {
                renderCostSection(sb, (MethodNode) node, highlighted);
            }

            if (node instanceof MethodNode) {
                renderMethodNode(sb, (MethodNode) node);
            } else if (node instanceof ThreadNode) {
                renderThreadNode(sb, (ThreadNode) node);
            } else if (node instanceof ThrowNode) {
                renderThrowNode(sb, (ThrowNode) node);
            } else {
                throw new UnsupportedOperationException("unknown trace node: " + node.getClass());
            }
        }

        private void renderCostSection(StringBuilder sb, MethodNode methodNode, Ansi highlighted) {
            String costStr = renderCost(methodNode);
            if (methodNode == maxCostNode) {
                sb.append(highlighted.a(costStr).reset().toString());
            } else {
                sb.append(costStr);
            }
        }

        private void renderMethodNode(StringBuilder sb, MethodNode methodNode) {
            sb.append(methodNode.getClassName()).append(":").append(methodNode.getMethodName()).append("()");
            if (methodNode.getLineNumber() != -1) {
                sb.append(" #").append(methodNode.getLineNumber());
            }
        }

        private void renderThreadNode(StringBuilder sb, ThreadNode threadNode) {
            sb.append(format("ts=%s;thread_name=%s;id=%d;is_daemon=%s;priority=%d;TCCL=%s",
                    DateUtils.formatDateTime(threadNode.getTimestamp()),
                    threadNode.getThreadName(),
                    threadNode.getThreadId(),
                    threadNode.isDaemon(),
                    threadNode.getPriority(),
                    threadNode.getClassloader()));

            if (threadNode.getTraceId() != null) {
                sb.append(";trace_id=").append(threadNode.getTraceId());
            }
            if (threadNode.getRpcId() != null) {
                sb.append(";rpc_id=").append(threadNode.getRpcId());
            }
        }

        private void renderThrowNode(StringBuilder sb, ThrowNode throwNode) {
            sb.append("throw:").append(throwNode.getException())
                    .append(" #").append(throwNode.getLineNumber())
                    .append(" [").append(throwNode.getMessage()).append("]");
        }
}
