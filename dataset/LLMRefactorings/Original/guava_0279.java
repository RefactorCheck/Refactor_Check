public class guava_0279 {

      @CanIgnoreReturnValue // TODO(b/219820829): consider removing
      public static HostAndPort fromString(String hostPortString) {
        checkNotNull(hostPortString);
        String host;
        String portString = null;
        boolean hasBracketlessColons = false;
    
        if (hostPortString.startsWith("[")) {
          String[] hostAndPort = getHostAndPortFromBracketedHost(hostPortString);
          host = hostAndPort[0];
          portString = hostAndPort[1];
        } else {
          int colonPos = hostPortString.indexOf(':');
          if (colonPos >= 0 && hostPortString.indexOf(':', colonPos + 1) == -1) {
            // Exactly 1 colon. Split into host:port.
            host = hostPortString.substring(0, colonPos);
            portString = hostPortString.substring(colonPos + 1);
          } else {
            // 0 or 2+ colons. Bare hostname or IPv6 literal.
            host = hostPortString;
            hasBracketlessColons = colonPos >= 0;
          }
        }
    
        Integer port;
        if (isNullOrEmpty(portString)) {
          port = NO_PORT;
        } else {
          port = Ints.tryParse(portString);
          checkArgument(port != null, "Unparseable port number: %s", hostPortString);
          checkArgument(isValidPort(port), "Port number out of range: %s", hostPortString);
        }
    
        return new HostAndPort(host, port, hasBracketlessColons);
      }
}
