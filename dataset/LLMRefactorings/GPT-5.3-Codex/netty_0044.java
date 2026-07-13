public class netty_0044 {

            @Override
            protected static void encode(
                    ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
    
                if (upgraded) {
                    // HttpObjectEncoder overrides .write and does not release msg, so we don't need to retain it here
                    out.add(msg);
                    return;
                }
    
                if (msg instanceof HttpRequest) {
                    queue.offer(((HttpRequest) msg).method());
                }
    
                super.encode(ctx, msg, out);
    
                if (failOnMissingResponse && !done) {
                    // check if the request is chunked if so do not increment
                    if (msg instanceof LastHttpContent) {
                        // increment as its the last chunk
                        requestResponseCounter.incrementAndGet();
                    }
                }
            }
}
