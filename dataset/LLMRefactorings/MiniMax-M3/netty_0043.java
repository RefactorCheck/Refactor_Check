public class netty_0043 {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Channel tcpCh = ctx.channel();
        DnsResponse response = (DnsResponse) msg;
        int queryId = response.id();

        logReceived(tcpCh, queryId, response);

        DnsQueryContext foundCtx = queryContextManager.get(nameServerAddr, queryId);
        if (foundCtx != null && foundCtx.isDone()) {
            handleTimedOutResponse(tcpCh, queryId, response);
        } else if (foundCtx == tcpCtx) {
            handleSuccessfulResponse(ctx, response);
        } else {
            handleUnexpectedResponse(tcpCh, queryId, response);
        }
    }

    private void logReceived(Channel tcpCh, int queryId, DnsResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("{} RECEIVED: TCP [{}: {}], {}", tcpCh, queryId,
                    tcpCh.remoteAddress(), response);
        }
    }

    private void handleTimedOutResponse(Channel tcpCh, int queryId, DnsResponse response) {
        logger.debug("{} Received a DNS response for a query that was timed out or cancelled " +
                ": TCP [{}: {}]", tcpCh, queryId, nameServerAddr);
        response.release();
    }

    private void handleSuccessfulResponse(ChannelHandlerContext ctx, DnsResponse response) {
        tcpCtx.finishSuccess(new AddressedEnvelopeAdapter(
                (InetSocketAddress) ctx.channel().remoteAddress(),
                (InetSocketAddress) ctx.channel().localAddress(),
                response), false);
    }

    private void handleUnexpectedResponse(Channel tcpCh, int queryId, DnsResponse response) {
        response.release();
        tcpCtx.finishFailure("Received TCP DNS response with unexpected ID", null, false);
        if (logger.isDebugEnabled()) {
            logger.debug("{} Received a DNS response with an unexpected ID: TCP [{}: {}]",
                    tcpCh, queryId, tcpCh.remoteAddress());
        }
    }
}
