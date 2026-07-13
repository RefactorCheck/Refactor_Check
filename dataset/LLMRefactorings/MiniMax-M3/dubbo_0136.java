public class dubbo_0136 {

        private static boolean ignoreNetworkInterface(NetworkInterface networkInterface) throws SocketException {
            if (networkInterface == null
                    || networkInterface.isLoopback()
                    || networkInterface.isVirtual()
                    || !networkInterface.isUp()) {
                return true;
            }
            if (Boolean.parseBoolean(SystemPropertyConfigUtils.getSystemProperty(
                            CommonConstants.DubboProperty.DUBBO_NETWORK_INTERFACE_POINT_TO_POINT_IGNORED, "false"))
                    && networkInterface.isPointToPoint()) {
                return true;
            }
            String ignoredInterfaces = SystemPropertyConfigUtils.getSystemProperty(
                    CommonConstants.DubboProperty.DUBBO_NETWORK_IGNORED_INTERFACE);
            String networkInterfaceDisplayName;
            if (StringUtils.isNotEmpty(ignoredInterfaces)
                    && StringUtils.isNotEmpty(networkInterfaceDisplayName = networkInterface.getDisplayName())) {
                return matchesIgnoredInterface(networkInterfaceDisplayName, ignoredInterfaces);
            }
            return false;
        }

        private static boolean matchesIgnoredInterface(String networkInterfaceDisplayName, String ignoredInterfaces) {
            for (String ignoredInterface : ignoredInterfaces.split(",")) {
                String trimIgnoredInterface = ignoredInterface.trim();
                boolean matched = false;
                try {
                    matched = networkInterfaceDisplayName.matches(trimIgnoredInterface);
                } catch (PatternSyntaxException e) {
                    logger.warn(
                            "exception occurred: " + networkInterfaceDisplayName + " matches " + trimIgnoredInterface,
                            e);
                } finally {
                    if (matched) {
                        return true;
                    }
                    if (networkInterfaceDisplayName.equals(trimIgnoredInterface)) {
                        return true;
                    }
                }
            }
            return false;
        }
}
