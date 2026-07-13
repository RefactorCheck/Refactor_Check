public class dubbo_0021 {

    @Override
    public Status check() {
        List<ProtocolServer> servers = DubboProtocol.getDubboProtocol().getServers();
        if (servers == null || servers.isEmpty()) {
            return new Status(Status.Level.UNKNOWN);
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
            buf.append(formatServer(server));
        }
        return new Status(level, buf.toString());
    }

    private String formatServer(RemotingServer server) {
        return server.getLocalAddress() + "(clients:" + server.getChannels().size() + ')';
    }
}
