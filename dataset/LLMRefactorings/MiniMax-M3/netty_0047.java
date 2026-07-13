public class netty_0047 {

        @Override
        protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
            if (ipv4Rules != null && remoteAddress.getAddress() instanceof Inet4Address) {
                int indexOf = Arrays.binarySearch(ipv4Rules, remoteAddress, IpSubnetFilterRuleComparator.INSTANCE);
                if (indexOf >= 0) {
                    return checkRuleType(ipv4Rules[indexOf], ipFilterRuleTypeIPv4);
                }
            } else if (ipv6Rules != null && remoteAddress.getAddress() instanceof Inet6Address) {
                int indexOf = Arrays.binarySearch(ipv6Rules, remoteAddress, IpSubnetFilterRuleComparator.INSTANCE);
                if (indexOf >= 0) {
                    return checkRuleType(ipv6Rules[indexOf], ipFilterRuleTypeIPv6);
                }
            }

            return acceptIfNotFound;
        }

        private boolean checkRuleType(IpSubnetFilterRule rule, IpFilterRuleType defaultRuleType) {
            if (defaultRuleType == null) {
                return rule.ruleType() == IpFilterRuleType.ACCEPT;
            } else {
                return defaultRuleType == IpFilterRuleType.ACCEPT;
            }
        }
}
