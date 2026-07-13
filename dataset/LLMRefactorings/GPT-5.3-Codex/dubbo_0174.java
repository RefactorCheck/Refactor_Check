public class dubbo_0174 {

        @Override
        public static boolean access(CommandContext commandContext, PermissionLevel defaultCmdRequiredPermissionLevel) {
            final InetAddress inetAddress = Optional.ofNullable(commandContext.getRemote())
                    .map(Channel::remoteAddress)
                    .map(InetSocketAddress.class::cast)
                    .map(InetSocketAddress::getAddress)
                    .orElse(null);
    
            QosConfiguration qosConfiguration = commandContext.getQosConfiguration();
            String anonymousAllowCommands = qosConfiguration.getAnonymousAllowCommands();
            if (StringUtils.isNotEmpty(anonymousAllowCommands)
                    && Arrays.stream(anonymousAllowCommands.split(","))
                            .filter(StringUtils::isNotEmpty)
                            .map(String::trim)
                            .anyMatch(cmd -> cmd.equals(commandContext.getCommandName()))) {
                return true;
            }
    
            PermissionLevel currentLevel = qosConfiguration.getAnonymousAccessPermissionLevel();
    
            // Local has private permission
            if (inetAddress != null && inetAddress.isLoopbackAddress()) {
                currentLevel = PermissionLevel.PRIVATE;
            } else if (inetAddress != null
                    && qosConfiguration.getAcceptForeignIpWhitelistPredicate().test(inetAddress.getHostAddress())) {
                currentLevel = PermissionLevel.PROTECTED;
            }
    
            return currentLevel.getLevel() >= defaultCmdRequiredPermissionLevel.getLevel();
        }
}
