public class netty_0267 {

        private void internalResolve(String name, Promise<List<T>> promise) {
            try {
                name = cnameResolveFromCache(cnameCache(), name);
            } catch (Throwable cause) {
                promise.tryFailure(cause);
                return;
            }
    
            try {
                DnsServerAddressStream nameServerAddressStream = getNameServers(name);
                doQuery(name, nameServerAddressStream, promise);
            } finally {
                channel.flush();
            }
        }
        
        private void doQuery(String name, DnsServerAddressStream nameServerAddressStream, Promise<List<T>> promise) {
            final int end = expectedTypes.length - 1;
            for (int i = 0; i < end; ++i) {
                if (!query(name, expectedTypes[i], nameServerAddressStream.duplicate(), false, promise)) {
                    return;
                }
            }
            query(name, expectedTypes[end], nameServerAddressStream, false, promise);
        }
}
