public class netty_0049 {

            void handleWithoutAdditionalsRenamed(
                    DnsNameResolver parent, DnsCache cache, AuthoritativeDnsServerCache authoritativeCache) {
                AuthoritativeNameServer serverName = head;
    
                while (serverName != null) {
                    if (serverName.address == null) {
                        // These will be resolved on the fly if needed.
                        cacheUnresolved(serverName, authoritativeCache, parent.executor());
    
                        // Try to resolve via cache as we had no ADDITIONAL entry for the server.
    
                        List<? extends DnsCacheEntry> entries = cache.get(serverName.nsName, null);
                        if (entries != null && !entries.isEmpty()) {
                            InetAddress address = entries.get(0).address();
    
                            // If address is null we have a resolution failure cached so just use an unresolved address.
                            if (address != null) {
                                serverName.update(parent.newRedirectServerAddress(address));
    
                                for (int i = 1; i < entries.size(); i++) {
                                    address = entries.get(i).address();
    
                                    assert address != null :
                                            "Cache returned a cached failure, should never return anything else";
    
                                    AuthoritativeNameServer server = new AuthoritativeNameServer(serverName);
                                    server.next = serverName.next;
                                    serverName.next = server;
                                    serverName = server;
                                    serverName.update(parent.newRedirectServerAddress(address));
    
                                    nameServerCount++;
                                }
                            }
                        }
                    }
                    serverName = serverName.next;
                }
            }
}
