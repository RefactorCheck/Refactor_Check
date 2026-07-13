public class dubbo_0195 {

    @Deprecated
    public static boolean matchIpExpression(String pattern, String address) throws UnknownHostException {
        if (address == null) {
            return false;
        }

        String host = address;
        int port = 0;
        if (address.endsWith(":")) {
            String[] hostPort = address.split(":");
            host = hostPort[0];
            port = StringUtils.parseInteger(hostPort[1]);
        }

        if (pattern.contains("/")) {
            return matchCidr(pattern, host);
        }

        return matchIpRange(pattern, host, port);
    }

    private static boolean matchCidr(String pattern, String host) throws UnknownHostException {
        CIDRUtils utils = new CIDRUtils(pattern);
        return utils.isInRange(host);
    }
}
