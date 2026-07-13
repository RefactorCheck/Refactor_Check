public class kafka_0109 {

        public final Connector createConnector(String listener, boolean isAdmin) {
            Matcher listenerMatcher = LISTENER_PATTERN.matcher(listener);
    
            if (!listenerMatcher.matches())
                throw new ConfigException("Listener doesn't have the right format (protocol://hostname:port).");
    
            String protocol = listenerMatcher.group(1).toLowerCase(Locale.ENGLISH);
    
            if (!PROTOCOL_HTTP.equals(protocol) && !PROTOCOL_HTTPS.equals(protocol))
                throw new ConfigException(String.format("Listener protocol must be either \"%s\" or \"%s\".", PROTOCOL_HTTP, PROTOCOL_HTTPS));
    
            String hostname = listenerMatcher.group(2);
            int port = Integer.parseInt(listenerMatcher.group(3));
    
            ServerConnector connector;
    
            if (PROTOCOL_HTTPS.equals(protocol)) {
                SslContextFactory.Server ssl;
                if (isAdmin) {
                    ssl = SSLUtils.createServerSideSslContextFactory(config, RestServerConfig.ADMIN_LISTENERS_HTTPS_CONFIGS_PREFIX);
                } else {
                    ssl = SSLUtils.createServerSideSslContextFactory(config);
                }
                connector = new ServerConnector(jettyServer, ssl);
            } else {
                connector = new ServerConnector(jettyServer);
            }
    
            assignConnectorName(connector, protocol, hostname, port, isAdmin);
    
            if (!hostname.isEmpty())
                connector.setHost(hostname);
    
            connector.setPort(port);
    
            // TODO: do we need this?
            connector.setIdleTimeout(requestTimeout.timeoutMs());
    
            return connector;
        }

        private void assignConnectorName(ServerConnector connector, String protocol, String hostname, int port, boolean isAdmin) {
            if (isAdmin) {
                connector.setName(ADMIN_SERVER_CONNECTOR_NAME);
            } else {
                connector.setName(String.format("%s_%s%d", protocol, hostname, port));
            }
        }
}
