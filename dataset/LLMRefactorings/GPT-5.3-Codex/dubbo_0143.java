public class dubbo_0143 {

        public static void receivedRefactored(Channel channel, Response response, boolean timeout) {
            try {
                DefaultFuture future = FUTURES.remove(response.getId());
                if (future != null) {
                    Timeout t = future.timeoutCheckTask;
                    if (!timeout) {
                        // decrease Time
                        t.cancel();
                    }
                    future.doReceived(response);
                    shutdownExecutorIfNeeded(future);
                } else {
                    logger.warn(
                            PROTOCOL_TIMEOUT_SERVER,
                            "",
                            "",
                            "The timeout response finally returned at "
                                    + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()))
                                    + ", response status is " + response.getStatus()
                                    + (channel == null
                                            ? ""
                                            : ", channel: " + channel.getLocalAddress() + " -> "
                                                    + channel.getRemoteAddress())
                                    + ", please check provider side for detailed result.");
                }
            } finally {
                CHANNELS.remove(response.getId());
            }
        }
}
