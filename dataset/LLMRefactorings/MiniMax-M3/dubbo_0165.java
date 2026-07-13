public class dubbo_0165 {

        private void closeReferenceCountExchangeClient(ReferenceCountExchangeClient client) {
            if (client == null) {
                return;
            }
    
            try {
                logConnectionClose(client);
                client.close(ConfigurationUtils.reCalShutdownTime(client.getShutdownWaitTime()));
            } catch (Throwable t) {
                logger.warn(PROTOCOL_ERROR_CLOSE_CLIENT, "", "", t.getMessage(), t);
            }
        }

        private void logConnectionClose(ReferenceCountExchangeClient client) {
            if (logger.isInfoEnabled()) {
                logger.info("Close dubbo connect: " + client.getLocalAddress() + "-->" + client.getRemoteAddress());
            }
        }
}
