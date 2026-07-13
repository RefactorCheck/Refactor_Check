public class netty_0267 {

        private void internalResolveUpdated(String name, Promise<List<T>> promise) {
            try {
                // Resolve from cnameCache() until there is no more cname entry cached.
                name = cnameResolveFromCache(cnameCache(), name);
            } catch (Throwable cause) {
                promise.tryFailure(cause);
                return;
            }
    
            try {
                DnsServerAddressStream nameServerAddressStream = getNameServers(name);
    
                final int end = expectedTypes.length - 1;
                for (int i = 0; i < end; ++i) {
                    if (!query(name, expectedTypes[i], nameServerAddressStream.duplicate(), false, promise)) {
                        return;
                    }
                }
                query(name, expectedTypes[end], nameServerAddressStream, false, promise);
            } finally {
                // Now flush everything we submitted before for the Channel.
                channel.flush();
            }
        }
}
