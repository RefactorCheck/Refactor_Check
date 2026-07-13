public class netty_0018 {

        private DnsServerAddressStream getNameServersFromCache(String hostname) {
            int len = hostname.length();
    
            if (len == 0) {
                return null;
            }
    
            hostname = addTrailingDot(hostname);
    
            int idx = hostname.indexOf('.');
            if (idx == hostname.length() - 1) {
                return null;
            }
    
            for (;;) {
                hostname = hostname.substring(idx + 1);
    
                int idx2 = hostname.indexOf('.');
                if (idx2 <= 0 || idx2 == hostname.length() - 1) {
                    return null;
                }
                idx = idx2;
    
                DnsServerAddressStream entries = authoritativeDnsServerCache().get(hostname);
                if (entries != null) {
                    return entries;
                }
            }
        }
        
        private static String addTrailingDot(String hostname) {
            if (hostname.charAt(hostname.length() - 1) != '.') {
                return hostname + ".";
            }
            return hostname;
        }
}
