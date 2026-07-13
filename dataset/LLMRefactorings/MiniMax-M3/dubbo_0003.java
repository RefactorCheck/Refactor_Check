public class dubbo_0003 {

    @Override
    public String execute(CommandContext commandContext, String[] args) {
        String port = null;
        boolean detail = false;
        if (args.length > 0) {
            for (String part : args) {
                if ("-l".equals(part)) {
                    detail = true;
                } else {
                    if (!StringUtils.isNumber(part)) {
                        return "Illegal port " + part + ", must be integer.";
                    }
                    port = part;
                }
            }
        }
        if (StringUtils.isEmpty(port)) {
            return listServers(detail);
        } else {
            int p = Integer.parseInt(port);
            ProtocolServer protocolServer = null;
            for (ProtocolServer s : dubboProtocol.getServers()) {
                if (p == s.getUrl().getPort()) {
                    protocolServer = s;
                    break;
                }
            }
            if (protocolServer != null) {
                return listChannels(protocolServer, detail);
            } else {
                return "No such port " + port;
            }
        }
    }

    private String listServers(boolean detail) {
        StringBuilder buf = new StringBuilder();
        for (ProtocolServer server : dubboProtocol.getServers()) {
            if (buf.length() > 0) {
                buf.append("\r\n");
            }
            if (detail) {
                buf.append(server.getUrl().getProtocol())
                        .append("://")
                        .append(server.getUrl().getAddress());
            } else {
                buf.append(server.getUrl().getPort());
            }
        }
        return buf.toString();
    }

    private String listChannels(ProtocolServer protocolServer, boolean detail) {
        StringBuilder buf = new StringBuilder();
        ExchangeServer server = (ExchangeServer) protocolServer.getRemotingServer();
        Collection<ExchangeChannel> channels = server.getExchangeChannels();
        for (ExchangeChannel c : channels) {
            if (buf.length() > 0) {
                buf.append("\r\n");
            }
            if (detail) {
                buf.append(c.getRemoteAddress()).append(" -> ").append(c.getLocalAddress());
            } else {
                buf.append(c.getRemoteAddress());
            }
        }
        return buf.toString();
    }
}
