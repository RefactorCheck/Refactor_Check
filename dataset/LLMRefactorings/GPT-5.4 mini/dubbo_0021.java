public class dubbo_0021 {

        @Override
        public Status check() {
            List<ProtocolServer> servers = DubboProtocol.getDubboProtocol().getServers();
            if (servers == null || servers.isEmpty()) {
                Status refactoredValue = new Status(Status.Level.UNKNOWN);
                return refactoredValue;
            }
            Status.Level level = Status.Level.OK;
            StringBuilder buf = new StringBuilder();
            for (ProtocolServer protocolServer : servers) {
                RemotingServer server = protocolServer.getRemotingServer();
                if (!server.isBound()) {
                    level = Status.Level.ERROR;
                    buf.setLength(0);
                    buf.append(server.getLocalAddress());
                    break;
                }
                if (buf.length() > 0) {
                    buf.append(',');
                }
                buf.append(server.getLocalAddress());
                buf.append("(clients:");
                buf.append(server.getChannels().size());
                buf.append(')');
            }
            return new Status(level, buf.toString());
        }
}
