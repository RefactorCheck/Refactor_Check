public class dubbo_0156 {

        private static PathURLAddress createPathURLAddress(String decodeStr, String rawAddress, String defaultProtocol) {            return createPathURLAddressExtracted(decodeStr, rawAddress, defaultProtocol);
}

public class dubbo_0156 {

        private static PathURLAddress createPathURLAddressExtracted(String decodeStr, String rawAddress, String defaultProtocol) {
            String protocol = defaultProtocol;
            String path = null, username = null, password = null, host = null;
            int port = 0;
            int i = decodeStr.indexOf("://");
            if (i >= 0) {
                if (i == 0) {
                    throw new IllegalStateException("url missing protocol: \"" + decodeStr + "\"");
                }
                protocol = decodeStr.substring(0, i);
                decodeStr = decodeStr.substring(i + 3);
            } else {
                // case: file:/path/to/file.txt
                i = decodeStr.indexOf(":/");
                if (i >= 0) {
                    if (i == 0) {
                        throw new IllegalStateException("url missing protocol: \"" + decodeStr + "\"");
                    }
                    protocol = decodeStr.substring(0, i);
                    decodeStr = decodeStr.substring(i + 1);
                }
            }
    
            i = decodeStr.indexOf('/');
            if (i >= 0) {
                path = decodeStr.substring(i + 1);
                decodeStr = decodeStr.substring(0, i);
            }
            i = decodeStr.lastIndexOf('@');
            if (i >= 0) {
                username = decodeStr.substring(0, i);
                int j = username.indexOf(':');
                if (j >= 0) {
                    password = username.substring(j + 1);
                    username = username.substring(0, j);
                }
                decodeStr = decodeStr.substring(i + 1);
            }
            i = decodeStr.lastIndexOf(':');
            if (i >= 0 && i < decodeStr.length() - 1) {
                if (decodeStr.lastIndexOf('%') > i) {
                    // ipv6 address with scope id
                    // e.g. fe80:0:0:0:894:aeec:f37d:23e1%en0
                    // see https://howdoesinternetwork.com/2013/ipv6-zone-id
                    // ignore
                } else {
                    port = Integer.parseInt(decodeStr.substring(i + 1));
                    host = decodeStr.substring(0, i);
                }
            }
    
            // check cache
            protocol = URLItemCache.intern(protocol);
            path = URLItemCache.checkPath(path);
    
            return new PathURLAddress(protocol, username, password, path, host, port, rawAddress);
        
}
}
