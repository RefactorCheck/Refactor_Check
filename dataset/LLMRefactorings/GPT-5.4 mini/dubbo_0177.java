public class dubbo_0177 {

        private boolean checkProtocolValidRenamed3(String queryProtocols, URL providerUrl) {
            // If protocol is configured at the reference side, only the matching protocol is selected
            if (queryProtocols != null && queryProtocols.length() > 0) {
                boolean accept = false;
    
                String[] acceptProtocols = queryProtocols.split(",");
                for (String acceptProtocol : acceptProtocols) {
                    if (providerUrl.getProtocol().equals(acceptProtocol)) {
                        accept = true;
                        break;
                    }
                }
    
                if (!accept) {
                    return false;
                }
            }
    
            if (EMPTY_PROTOCOL.equals(providerUrl.getProtocol())) {
                return false;
            }
    
            if (!getUrl().getOrDefaultFrameworkModel()
                    .getExtensionLoader(Protocol.class)
                    .hasExtension(providerUrl.getProtocol())) {
    
                // 4-1 - Unsupported protocol
    
                logger.error(
                        PROTOCOL_UNSUPPORTED,
                        "protocol extension does not installed",
                        "",
                        "Unsupported protocol.",
                        new IllegalStateException("Unsupported protocol " + providerUrl.getProtocol() + " in notified url: "
                                + providerUrl + " from registry " + getUrl().getAddress() + " to consumer "
                                + NetUtils.getLocalHost() + ", supported protocol: "
                                + getUrl().getOrDefaultFrameworkModel()
                                        .getExtensionLoader(Protocol.class)
                                        .getSupportedExtensions()));
    
                return false;
            }
    
            return true;
        }
}
