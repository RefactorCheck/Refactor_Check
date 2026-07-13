public class arthas_0068 {

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status, McpError mcpError) {
        try {
            String jsonError = objectMapper.writeValueAsString(mcpError);
            ByteBuf content = Unpooled.copiedBuffer(jsonError, CharsetUtil.UTF_8);
            FullHttpResponse response = buildResponse(HttpVersion.HTTP_1_1, status, content, APPLICATION_JSON);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            logger.error(FAILED_TO_SEND_ERROR_RESPONSE, e.getMessage());
            FullHttpResponse response = buildResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, null, null);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private FullHttpResponse buildResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, String contentType) {
        FullHttpResponse response = content != null
                ? new DefaultFullHttpResponse(version, status, content)
                : new DefaultFullHttpResponse(version, status);
        if (contentType != null) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
        }
        int contentLength = content != null ? content.readableBytes() : 0;
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, contentLength);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        return response;
    }
}
