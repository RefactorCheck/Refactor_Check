public class springframework_0103 {

    private static boolean encoded(String hostName, boolean ipv6) {
        int length = hostName.length();
        for (int i = 0; i < length; i++) {
            char c = hostName.charAt(i);
            if (c == '%') {
                if (!isValidPercentEncoded(hostName, i, length)) {
                    return false;
                }
                i += 2;
            }
            else if (!isAllowedInHost(c, ipv6)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidPercentEncoded(String hostName, int i, int length) {
        if ((i + 2) >= length) {
            return false;
        }
        char hex1 = hostName.charAt(i + 1);
        char hex2 = hostName.charAt(i + 2);
        int u = Character.digit(hex1, 16);
        int l = Character.digit(hex2, 16);
        return u != -1 && l != -1;
    }
}
