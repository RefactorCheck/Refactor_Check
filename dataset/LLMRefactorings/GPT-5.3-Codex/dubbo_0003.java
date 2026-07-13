public class dubbo_0003 {

        @Override
        public String execute(CommandContext commandContext, String[] args) {
            StringBuilder buf = new StringBuilder();
            String port = null;
            boolean detail = false;
            if (args.length > 0) {
                for (String part : args) {
                    if ("-l".equals(part)) {
                        detail = true;
                    } else {
                        if (!StringUtils.isNumber(part)) {
                            String result = "Illegal port " + part + ", must be integer.";
                            return result;
                        }
                        port = part;
                    }
                }
            }
            if (StringUtils.isEmpty(port)) {
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
                } else {
                    buf.append("No such port ").append(port);
                }
            }
            return buf.toString();
        }
}
