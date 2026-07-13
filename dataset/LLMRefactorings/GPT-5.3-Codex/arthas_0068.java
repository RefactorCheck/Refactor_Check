public class arthas_0068 {
        private String jsonError;


        private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status, McpError mcpError) {
            try {
                jsonError = objectMapper.writeValueAsString(mcpError);
                ByteBuf content = Unpooled.copiedBuffer(jsonError, CharsetUtil.UTF_8);
    
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        status,
                        content
                );
    
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, APPLICATION_JSON);
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
    
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            } catch (Exception e) {
                logger.error(FAILED_TO_SEND_ERROR_RESPONSE, e.getMessage());
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.INTERNAL_SERVER_ERROR
                );
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
        }
}
