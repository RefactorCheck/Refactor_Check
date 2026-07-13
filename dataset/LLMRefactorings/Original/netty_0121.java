public class netty_0121 {

            @Override
            public V map(String hostname) {
                if (hostname != null) {
                    hostname = normalize(hostname);
    
                    // Let's try an exact match first
                    V value = map.get(hostname);
                    if (value != null) {
                        return value;
                    }
    
                    // No exact match, let's try a wildcard match.
                    int idx = hostname.indexOf('.');
                    if (idx != -1) {
                        value = map.get(hostname.substring(idx));
                        if (value != null) {
                            return value;
                        }
                    }
                }
    
                return defaultValue;
            }
}
