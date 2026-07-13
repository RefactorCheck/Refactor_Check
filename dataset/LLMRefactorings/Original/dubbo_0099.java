public class dubbo_0099 {

        private void buildPredicate() {
            if (StringUtils.isNotEmpty(acceptForeignIpWhitelist)) {
                this.acceptForeignIpWhitelistPredicate = Arrays.stream(acceptForeignIpWhitelist.split(","))
                        .map(String::trim)
                        .filter(StringUtils::isNotEmpty)
                        .map(foreignIpPattern -> (Predicate<String>) foreignIp -> {
                            try {
                                // hard code port to -1
                                return NetUtils.matchIpExpression(foreignIpPattern, foreignIp, -1);
                            } catch (UnknownHostException ignore) {
                                // ignore illegal CIDR specification
                            }
                            return false;
                        })
                        .reduce(Predicate::or)
                        .orElse(s -> false);
            } else {
                this.acceptForeignIpWhitelistPredicate = foreignIp -> false;
            }
        }
}
