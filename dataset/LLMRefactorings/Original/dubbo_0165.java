public class dubbo_0165 {

        private void closeReferenceCountExchangeClient(ReferenceCountExchangeClient client) {
            if (client == null) {
                return;
            }
    
            try {
                if (logger.isInfoEnabled()) {
                    logger.info("Close dubbo connect: " + client.getLocalAddress() + "-->" + client.getRemoteAddress());
                }
    
                client.close(ConfigurationUtils.reCalShutdownTime(client.getShutdownWaitTime()));
    
                // TODO
                /*
                 * At this time, ReferenceCountExchangeClient#client has been replaced with LazyConnectExchangeClient.
                 * Do you need to call client.close again to ensure that LazyConnectExchangeClient is also closed?
                 */
    
            } catch (Throwable t) {
                logger.warn(PROTOCOL_ERROR_CLOSE_CLIENT, "", "", t.getMessage(), t);
            }
        }
}
