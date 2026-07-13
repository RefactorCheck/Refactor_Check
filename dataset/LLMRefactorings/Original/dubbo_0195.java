public class dubbo_0195 {

        @Deprecated
        public static boolean matchIpExpression(String pattern, String address) throws UnknownHostException {
            if (address == null) {
                return false;
            }
    
            String host = address;
            int port = 0;
            // only works for ipv4 address with 'ip:port' format
            if (address.endsWith(":")) {
                String[] hostPort = address.split(":");
                host = hostPort[0];
                port = StringUtils.parseInteger(hostPort[1]);
            }
    
            // if the pattern is subnet format, it will not be allowed to config port param in pattern.
            if (pattern.contains("/")) {
                CIDRUtils utils = new CIDRUtils(pattern);
                return utils.isInRange(host);
            }
    
            return matchIpRange(pattern, host, port);
        }
}
