public class arthas_0118 {

        private void renderNode(StringBuilder sb, TraceNode node, Ansi highlighted) {
            //render cost: [0.366865ms]
            if (isPrintCost && node instanceof MethodNode) {
                MethodNode methodNode = (MethodNode) node;
    
                String costStr = renderCost(methodNode);
                if (node == maxCostNode) {
                    // the node with max cost will be highlighted
                    sb.append(highlighted.a(costStr).reset().toString());
                } else {
                    sb.append(costStr);
                }
            }
    
            //render method name
            if (node instanceof MethodNode) {
                MethodNode methodNode = (MethodNode) node;
                //clazz.getName() + ":" + method.getName() + "()"
                sb.append(methodNode.getClassName()).append(":").append(methodNode.getMethodName()).append("()");
                // #lineNumber
                if (methodNode.getLineNumber()!= -1) {
                    sb.append(" #").append(methodNode.getLineNumber());
                }
            } else if (node instanceof ThreadNode) {
                //render thread info
                ThreadNode threadNode = (ThreadNode) node;
                //ts=2020-04-29 10:34:00;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@18b4aac2
                sb.append(format("ts=%s;thread_name=%s;id=%d;is_daemon=%s;priority=%d;TCCL=%s",
                        DateUtils.formatDateTime(threadNode.getTimestamp()),
                        threadNode.getThreadName(),
                        threadNode.getThreadId(),
                        threadNode.isDaemon(),
                        threadNode.getPriority(),
                        threadNode.getClassloader()));
    
                //trace_id
                if (threadNode.getTraceId() != null) {
                    sb.append(";trace_id=").append(threadNode.getTraceId());
                }
                if (threadNode.getRpcId() != null) {
                    sb.append(";rpc_id=").append(threadNode.getRpcId());
                }
            } else if (node instanceof ThrowNode) {
                ThrowNode throwNode = (ThrowNode) node;
                sb.append("throw:").append(throwNode.getException())
                        .append(" #").append(throwNode.getLineNumber())
                        .append(" [").append(throwNode.getMessage()).append("]");
    
            } else {
                throw new UnsupportedOperationException("unknown trace node: " + node.getClass());
            }
        }
}
