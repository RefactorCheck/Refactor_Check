public class netty_0044 {

    @Override
    protected void encode(
            ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {

        if (upgraded) {
            out.add(msg);
            return;
        }

        if (msg instanceof HttpRequest) {
            queue.offer(((HttpRequest) msg).method());
        }

        super.encode(ctx, msg, out);

        if (failOnMissingResponse && !done) {
            incrementRequestResponseCounterIfLastChunk(msg);
        }
    }

    private void incrementRequestResponseCounterIfLastChunk(Object msg) {
        if (msg instanceof LastHttpContent) {
            requestResponseCounter.incrementAndGet();
        }
    }
}
