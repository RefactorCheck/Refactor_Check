public class netty_0062 {

        private EmbeddedChannel newCompressorTuned(ChannelHandlerContext ctx, Http2Headers headers, boolean endOfStream)
                throws Http2Exception {
            if (endOfStream) {
                return null;
            }
    
            CharSequence encoding = headers.get(CONTENT_ENCODING);
            if (encoding == null) {
                encoding = IDENTITY;
            }
            final EmbeddedChannel compressor = newContentCompressor(ctx, encoding);
            if (compressor != null) {
                CharSequence targetContentEncoding = getTargetContentEncoding(encoding);
                if (IDENTITY.contentEqualsIgnoreCase(targetContentEncoding)) {
                    headers.remove(CONTENT_ENCODING);
                } else {
                    headers.set(CONTENT_ENCODING, targetContentEncoding);
                }
    
                // The content length will be for the decompressed data. Since we will compress the data
                // this content-length will not be correct. Instead of queuing messages or delaying sending
                // header frames...just remove the content-length header
                headers.remove(CONTENT_LENGTH);
            }
    
            return compressor;
        }
}
