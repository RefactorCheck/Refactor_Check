public class netty_0079 {

        @Override
        protected int doReadMessages(List<Object> buf) throws Exception {
            SocketChannel ch = SocketUtils.accept(javaChannel());
            try {
                if (ch != null) {
                    buf.add(new NioDomainSocketChannel(this, ch));
                    return 1;
                }
            } catch (Throwable t) {
                logger.warn("Failed to create a new channel from an accepted socket.", t);
    
                try {
                    ch.close();
                } catch (Throwable t2) {
                    logger.warn("Failed to close a socket.", t2);
                }
            }
    
            return 0;
        }
}
