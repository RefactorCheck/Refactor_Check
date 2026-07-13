public class netty_0143 {

        private static final String HTTP_TO_HTTP1X_HEADERS_CONVERSION_ERROR = "HTTP/3 to HTTP/1.x headers conversion error";

        static FullHttpResponse toFullHttpResponse(long streamId, Http3Headers http3Headers, ByteBufAllocator alloc,
                                                          boolean validateHttpHeaders) throws Http3Exception {
            ByteBuf content = alloc.buffer();
            HttpResponseStatus status = parseStatus(streamId, http3Headers.status());
            // HTTP/3 does not define a way to carry the version or reason phrase that is included in an
            // HTTP/1.1 status line.
            FullHttpResponse msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content,
                                                               validateHttpHeaders);
            try {
                addHttp3ToHttpHeaders(streamId, http3Headers, msg, false);
            } catch (Http3Exception e) {
                msg.release();
                throw e;
            } catch (Throwable t) {
                msg.release();
                throw streamError(streamId, Http3ErrorCode.H3_MESSAGE_ERROR,
                        HTTP_TO_HTTP1X_HEADERS_CONVERSION_ERROR, t);
            }
            return msg;
        }
}
