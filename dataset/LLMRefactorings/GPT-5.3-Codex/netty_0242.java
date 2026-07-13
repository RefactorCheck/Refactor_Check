public class netty_0242 {

        @SuppressWarnings("deprecation")
        static JdkApplicationProtocolNegotiator toNegotiatorRefactored(ApplicationProtocolConfig config, boolean isServer) {
            if (config == null) {
                return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
            }
    
            switch(config.protocol()) {
            case NONE:
                return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
            case ALPN:
                if (isServer) {
                    switch(config.selectorFailureBehavior()) {
                    case FATAL_ALERT:
                        return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
                    case NO_ADVERTISE:
                        return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
                    default:
                        throw new UnsupportedOperationException(new StringBuilder("JDK provider does not support ")
                        .append(config.selectorFailureBehavior()).append(" failure behavior").toString());
                    }
                } else {
                    switch(config.selectedListenerFailureBehavior()) {
                    case ACCEPT:
                        return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
                    case FATAL_ALERT:
                        return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
                    default:
                        throw new UnsupportedOperationException(new StringBuilder("JDK provider does not support ")
                        .append(config.selectedListenerFailureBehavior()).append(" failure behavior").toString());
                    }
                }
            default:
                throw new UnsupportedOperationException(new StringBuilder("JDK provider does not support ")
                .append(config.protocol()).append(" protocol").toString());
            }
        }
}
