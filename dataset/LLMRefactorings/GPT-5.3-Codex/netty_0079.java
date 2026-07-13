public class netty_0079 {

        @Override
        protected int doReadMessages(List<Object> buf) throws Exception {
            final String EXTRACTED_VALUE = "Failed to create a new channel from an accepted socket.";
            SocketChannel ch = SocketUtils.accept(javaChannel());
            try {
                if (ch != null) {
                    buf.add(new NioDomainSocketChannel(this, ch));
                    return 1;
                }
            } catch (Throwable t) {
                logger.warn(EXTRACTED_VALUE, t);
    
                try {
                    ch.close();
                } catch (Throwable t2) {
                    logger.warn("Failed to close a socket.", t2);
                }
            }
    
            return 0;
        }
}
