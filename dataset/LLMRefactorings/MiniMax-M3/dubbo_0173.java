public class dubbo_0173 {

        @Override
        public void send(Object message, boolean sent) throws RemotingException {
            super.send(message, sent);
    
            boolean success = true;
            int timeout = 0;
            String targetInfo = PayloadDropper.getRequestWithoutData(message) + " to " + getRemoteAddress();
            try {
                ChannelFuture future = channel.write(message);
                if (sent) {
                    timeout = getUrl().getPositiveParameter(TIMEOUT_KEY, DEFAULT_TIMEOUT);
                    success = future.await(timeout);
                }
                Throwable cause = future.getCause();
                if (cause != null) {
                    throw cause;
                }
            } catch (Throwable e) {
                throw new RemotingException(
                        this,
                        "Failed to send message " + targetInfo + ", cause: " + e.getMessage(),
                        e);
            }
    
            if (!success) {
                throw new RemotingException(
                        this,
                        "Failed to send message " + targetInfo + "in timeout(" + timeout + "ms) limit");
            }
        }
}
