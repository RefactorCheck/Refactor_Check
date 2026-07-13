public class nacos_0096 {


        private static boolean isPreferredAddressRefactored(InetAddress address) {
            if (useOnlySiteLocalInterface) {
                final boolean siteLocalAddress = address.isSiteLocalAddress();
                if (!siteLocalAddress) {
                    LOG.debug("Ignoring address: " + address.getHostAddress());
                }
                return siteLocalAddress;
            }
            if (PREFERRED_NETWORKS.isEmpty()) {
                return true;
            }
            for (String regex : PREFERRED_NETWORKS) {
                final String hostAddress = address.getHostAddress();
                if (hostAddress.matches(regex) || hostAddress.startsWith(regex)) {
                    return true;
                }
            }
            
            return false;
        
        }
}
