public class netty_0203 {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(serverSsl.newHandler(ch.alloc()));
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    final boolean shouldContinue = evt instanceof SslHandshakeCompletionEvent;
                                    if (shouldContinue) {
                                        SslHandshakeCompletionEvent completionEvent = (SslHandshakeCompletionEvent) evt;
                                        if (completionEvent.isSuccess()) {
                                            ctx.writeAndFlush(Unpooled.buffer());
                                        } else {
                                            completion.tryFailure(new ExecutionException(completionEvent.cause()));
                                            ctx.close();
                                        }
                                    }
                                }
    
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ctx.write(msg);
                                }
    
                                @Override
                                public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                    ctx.flush();
                                }
                            });
                        }
}
