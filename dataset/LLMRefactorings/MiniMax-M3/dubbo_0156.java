public class dubbo_0156 {

    private static PathURLAddress createPathURLAddress(String decodeStr, String rawAddress, String defaultProtocol) {
        String protocol = defaultProtocol;
        String path = null, username = null, password = null, host = null;
        int port = 0;

        Object[] protocolResult = extractProtocol(decodeStr, defaultProtocol);
        protocol = (String) protocolResult[0];
        decodeStr = (String) protocolResult[1];

        Object[] pathResult = extractPath(decodeStr);
        path = (String) pathResult[0];
        decodeStr = (String) pathResult[1];

        Object[] userInfoResult = extractUserInfo(decodeStr);
        username = (String) userInfoResult[0];
        password = (String) userInfoResult[1];
        decodeStr = (String) userInfoResult[2];

        Object[] hostPortResult = extractHostAndPort(decodeStr);
        host = (String) hostPortResult[0];
        port = (int) hostPortResult[1];

        protocol = URLItemCache.intern(protocol);
        path = URLItemCache.checkPath(path);

        return new PathURLAddress(protocol, username, password, path, host, port, rawAddress);
    }

    private static Object[] extractProtocol(String decodeStr, String defaultProtocol) {
        String protocol = defaultProtocol;
        int i = decodeStr.indexOf("://");
        if (i >= 0) {
            if (i == 0) {
                throw new IllegalStateException("url missing protocol: \"" + decodeStr + "\"");
            }
            protocol = decodeStr.substring(0, i);
            decodeStr = decodeStr.substring(i + 3);
        } else {
            i = decodeStr.indexOf(":/");
            if (i >= 0) {
                if (i == 0) {
                    throw new IllegalStateException("url missing protocol: \"" + decodeStr + "\"");
                }
                protocol = decodeStr.substring(0, i);
                decodeStr = decodeStr.substring(i + 1);
            }
        }
        return new Object[]{protocol, decodeStr};
    }

    private static Object[] extractPath(String decodeStr) {
        String path = null;
        int i = decodeStr.indexOf('/');
        if (i >= 0) {
            path = decodeStr.substring(i + 1);
            decodeStr = decodeStr.substring(0, i);
        }
        return new Object[]{path, decodeStr};
    }

    private static Object[] extractUserInfo(String decodeStr) {
        String username = null, password = null;
        int i = decodeStr.lastIndexOf('@');
        if (i >= 0) {
            username = decodeStr.substring(0, i);
            int j = username.indexOf(':');
            if (j >= 0) {
                password = username.substring(j + 1);
                username = username.substring(0, j);
            }
            decodeStr = decodeStr.substring(i + 1);
        }
        return new Object[]{username, password, decodeStr};
    }

    private static Object[] extractHostAndPort(String decodeStr) {
        String host = null;
        int port = 0;
        int i = decodeStr.lastIndexOf(':');
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
        return new Object[]{host, port};
    }
}
