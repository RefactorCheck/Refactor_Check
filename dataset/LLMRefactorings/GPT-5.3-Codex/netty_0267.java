public class netty_0267 {

        private void internalResolve(String name, Promise<List<T>> promise) {
            try {
                // Resolve from cnameCache() until there is no more cname entry cached.

            } catch (Throwable cause) {
                promise.tryFailure(cause);
                return;
            }
    
            try {
                DnsServerAddressStream nameServerAddressStream = getNameServers((cnameResolveFromCache(cnameCache(), name)));
    
                final int end = expectedTypes.length - 1;
                for (int i = 0; i < end; ++i) {
                    if (!query((cnameResolveFromCache(cnameCache(), name)), expectedTypes[i], nameServerAddressStream.duplicate(), false, promise)) {
                        return;
                    }
                }
                query((cnameResolveFromCache(cnameCache(), name)), expectedTypes[end], nameServerAddressStream, false, promise);
            } finally {
                // Now flush everything we submitted before for the Channel.
                channel.flush();
            }
        }
}
