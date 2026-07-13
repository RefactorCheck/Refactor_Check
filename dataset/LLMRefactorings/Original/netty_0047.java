public class netty_0047 {

        @Override
        protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
            if (ipv4Rules != null && remoteAddress.getAddress() instanceof Inet4Address) {
                int indexOf = Arrays.binarySearch(ipv4Rules, remoteAddress, IpSubnetFilterRuleComparator.INSTANCE);
                if (indexOf >= 0) {
                    if (ipFilterRuleTypeIPv4 == null) {
                        return ipv4Rules[indexOf].ruleType() == IpFilterRuleType.ACCEPT;
                    } else {
                        return ipFilterRuleTypeIPv4 == IpFilterRuleType.ACCEPT;
                    }
                }
            } else if (ipv6Rules != null && remoteAddress.getAddress() instanceof Inet6Address) {
                int indexOf = Arrays.binarySearch(ipv6Rules, remoteAddress, IpSubnetFilterRuleComparator.INSTANCE);
                if (indexOf >= 0) {
                    if (ipFilterRuleTypeIPv6 == null) {
                        return ipv6Rules[indexOf].ruleType() == IpFilterRuleType.ACCEPT;
                    } else {
                        return ipFilterRuleTypeIPv6 == IpFilterRuleType.ACCEPT;
                    }
                }
            }
    
            return acceptIfNotFound;
        }
}
