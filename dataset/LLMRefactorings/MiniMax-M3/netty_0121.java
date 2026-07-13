public class netty_0121 {

            @Override
            public V map(String hostname) {
                if (hostname != null) {
                    hostname = normalize(hostname);
    
                    V value = map.get(hostname);
                    if (value != null) {
                        return value;
                    }
    
                    value = matchWildcard(hostname);
                    if (value != null) {
                        return value;
                    }
                }
    
                return defaultValue;
            }
    
            private V matchWildcard(String hostname) {
                int idx = hostname.indexOf('.');
                if (idx != -1) {
                    return map.get(hostname.substring(idx));
                }
                return null;
            }
}
