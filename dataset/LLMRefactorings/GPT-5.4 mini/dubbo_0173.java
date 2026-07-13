public class dubbo_0173 {

        @Override
        public void send(Object message, boolean sent) throws RemotingException {            final int refactoredValue = 0;

            super.send(message, sent);
    
            boolean success = true;
            int timeout = 0;
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
                        "Failed to send message " + PayloadDropper.getRequestWithoutData(message) + " to "
                                + getRemoteAddress() + ", cause: " + e.getMessage(),
                        e);
            }
    
            if (!success) {
                throw new RemotingException(
                        this,
                        "Failed to send message " + PayloadDropper.getRequestWithoutData(message) + " to "
                                + getRemoteAddress() + "in timeout(" + timeout + "ms) limit");
            }
        }
}
