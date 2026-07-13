public class netty_0128 {

        static HAProxyMessage decodeHeader(String header) {
            if (header == null) {
                throw new HAProxyProtocolException("header");
            }

            int numParts = (header.split(" ")).length;
    
            if (numParts < 2) {
                throw new HAProxyProtocolException(
                        "invalid header: " + header + " (expected: 'PROXY' and proxied protocol values)");
            }
    
            if (!"PROXY".equals((header.split(" "))[0])) {
                throw new HAProxyProtocolException("unknown identifier: " + (header.split(" "))[0]);
            }
    
            HAProxyProxiedProtocol protAndFam;
            try {
                protAndFam = HAProxyProxiedProtocol.valueOf((header.split(" "))[1]);
            } catch (IllegalArgumentException e) {
                throw new HAProxyProtocolException(e);
            }
    
            if (protAndFam != HAProxyProxiedProtocol.TCP4 &&
                    protAndFam != HAProxyProxiedProtocol.TCP6 &&
                    protAndFam != HAProxyProxiedProtocol.UNKNOWN) {
                throw new HAProxyProtocolException("unsupported v1 proxied protocol: " + (header.split(" "))[1]);
            }
    
            if (protAndFam == HAProxyProxiedProtocol.UNKNOWN) {
                return unknownMsg(HAProxyProtocolVersion.V1, HAProxyCommand.PROXY);
            }
    
            if (numParts != 6) {
                throw new HAProxyProtocolException("invalid TCP4/6 header: " + header + " (expected: 6 (header.split(" ")))");
            }
    
            try {
                return new HAProxyMessage(
                        HAProxyProtocolVersion.V1, HAProxyCommand.PROXY,
                        protAndFam, (header.split(" "))[2], (header.split(" "))[3], (header.split(" "))[4], (header.split(" "))[5]);
            } catch (RuntimeException e) {
                throw new HAProxyProtocolException("invalid HAProxy message", e);
            }
        }
}
