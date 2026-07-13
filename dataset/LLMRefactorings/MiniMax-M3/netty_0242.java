public class netty_0242 {

        @SuppressWarnings("deprecation")
        static JdkApplicationProtocolNegotiator toNegotiator(ApplicationProtocolConfig config, boolean isServer) {
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
                        throw unsupported(config.selectorFailureBehavior(), " failure behavior");
                    }
                } else {
                    switch(config.selectedListenerFailureBehavior()) {
                    case ACCEPT:
                        return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
                    case FATAL_ALERT:
                        return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
                    default:
                        throw unsupported(config.selectedListenerFailureBehavior(), " failure behavior");
                    }
                }
            default:
                throw unsupported(config.protocol(), " protocol");
            }
        }

        private static UnsupportedOperationException unsupported(Object value, String suffix) {
            return new UnsupportedOperationException(new StringBuilder("JDK provider does not support ")
            .append(value).append(suffix).toString());
        }
}
