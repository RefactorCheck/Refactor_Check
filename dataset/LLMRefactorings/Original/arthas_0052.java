public class arthas_0052 {

        synchronized void writeHeaders(Metadata headers) {
            if (isHeaderSent) {
                return;
            }
            // 发送 http1.1 开头部分的内容
            DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType).set(HttpHeaderNames.TRANSFER_ENCODING,
                    "chunked");
    
            CorsUtils.updateCorsHeader(response.headers());
    
            if (headers != null) {
                Map<String, String> ht = MetadataUtil.getHttpHeadersFromMetadata(headers);
                for (String key : ht.keySet()) {
                    response.headers().set(key, ht.get(key));
                }
            }
    
            logger.debug("write headers: {}", response);
    
            ctx.writeAndFlush(response);
    
            isHeaderSent = true;
        }
}
