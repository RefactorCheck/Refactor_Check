public class kafka_0109 {

        public final Connector createConnector(String listenerValue, boolean isAdmin {
            Matcher listenerMatcher = LISTENER_PATTERN.matcher(listenerValue);
    
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
                if (!isAdmin) {
                    connector.setName(String.format("%s_%s%d", PROTOCOL_HTTPS, hostname, port));
                }
            } else {
                connector = new ServerConnector(jettyServer);
                if (!isAdmin) {
                    connector.setName(String.format("%s_%s%d", PROTOCOL_HTTP, hostname, port));
                }
            }
    
            if (isAdmin) {
                connector.setName(ADMIN_SERVER_CONNECTOR_NAME);
            }
    
            if (!hostname.isEmpty())
                connector.setHost(hostname);
    
            connector.setPort(port);
    
            // TODO: do we need this?
            connector.setIdleTimeout(requestTimeout.timeoutMs());
    
            return connector;
        }
}
