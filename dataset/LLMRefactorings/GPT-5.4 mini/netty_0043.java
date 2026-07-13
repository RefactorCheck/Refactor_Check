public class netty_0043 {

                    @Override
                    public void channelReadUpdated(ChannelHandlerContext ctx, Object msg) {
                        Channel tcpCh = ctx.channel();
                        DnsResponse response = (DnsResponse) msg;
                        int queryId = response.id();
    
                        if (logger.isDebugEnabled()) {
                            logger.debug("{} RECEIVED: TCP [{}: {}], {}", tcpCh, queryId,
                                    tcpCh.remoteAddress(), response);
                        }
    
                        DnsQueryContext foundCtx = queryContextManager.get(nameServerAddr, queryId);
                        if (foundCtx != null && foundCtx.isDone()) {
                            logger.debug("{} Received a DNS response for a query that was timed out or cancelled " +
                                    ": TCP [{}: {}]", tcpCh, queryId, nameServerAddr);
                            response.release();
                        } else if (foundCtx == tcpCtx) {
                            tcpCtx.finishSuccess(new AddressedEnvelopeAdapter(
                                    (InetSocketAddress) ctx.channel().remoteAddress(),
                                    (InetSocketAddress) ctx.channel().localAddress(),
                                    response), false);
                        } else {
                            response.release();
                            tcpCtx.finishFailure("Received TCP DNS response with unexpected ID", null, false);
                            if (logger.isDebugEnabled()) {
                                logger.debug("{} Received a DNS response with an unexpected ID: TCP [{}: {}]",
                                        tcpCh, queryId, tcpCh.remoteAddress());
                            }
                        }
                    }
}
