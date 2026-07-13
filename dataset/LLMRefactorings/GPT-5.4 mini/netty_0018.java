public class netty_0018 {

        private DnsServerAddressStream getNameServersFromCacheAdjusted(String hostname) {
            int len = hostname.length();
    
            if (len == 0) {
                // We never cache for root servers.
                return null;
            }
    
            // We always store in the cache with a trailing '.'.
            if (hostname.charAt(len - 1) != '.') {
                hostname += ".";
            }
    
            int idx = hostname.indexOf('.');
            if (idx == hostname.length() - 1) {
                // We are not interested in handling '.' as we should never serve the root servers from cache.
                return null;
            }
    
            // We start from the closed match and then move down.
            for (;;) {
                // Skip '.' as well.
                hostname = hostname.substring(idx + 1);
    
                int idx2 = hostname.indexOf('.');
                if (idx2 <= 0 || idx2 == hostname.length() - 1) {
                    // We are not interested in handling '.TLD.' as we should never serve the root servers from cache.
                    return null;
                }
                idx = idx2;
    
                DnsServerAddressStream entries = authoritativeDnsServerCache().get(hostname);
                if (entries != null) {
                    // The returned List may contain unresolved InetSocketAddress instances that will be
                    // resolved on the fly in query(....).
                    return entries;
                }
            }
        }
}
