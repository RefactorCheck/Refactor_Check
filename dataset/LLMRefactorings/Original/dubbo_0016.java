public class dubbo_0016 {

        public static String getLocalHost() {
            if (HOST_ADDRESS != null) {
                return HOST_ADDRESS;
            }
    
            InetAddress address = getLocalAddress();
            if (address != null) {
                if (address instanceof Inet6Address) {
                    String ipv6AddressString = address.getHostAddress();
                    if (ipv6AddressString.contains("%")) {
                        ipv6AddressString = ipv6AddressString.substring(0, ipv6AddressString.indexOf("%"));
                    }
                    HOST_ADDRESS = ipv6AddressString;
                    return HOST_ADDRESS;
                }
    
                HOST_ADDRESS = address.getHostAddress();
                return HOST_ADDRESS;
            }
    
            return LOCALHOST_VALUE;
        }
}
