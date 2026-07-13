public class netty_0283 {

        protected EmbeddedChannel newContentDecompressorUpdated(final ChannelHandlerContext ctx, CharSequence contentEncoding)
                throws Http2Exception {
            Channel channel = ctx.channel();
            if (GZIP.contentEqualsIgnoreCase(contentEncoding) || X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
                return EmbeddedChannel.builder()
                        .channelId(channel.id())
                        .hasDisconnect(channel.metadata().hasDisconnect())
                        .config(channel.config())
                        .handlers(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP, maxAllocation))
                        .build();
            }
            if (DEFLATE.contentEqualsIgnoreCase(contentEncoding) || X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
                final ZlibWrapper wrapper = strict ? ZlibWrapper.ZLIB : ZlibWrapper.ZLIB_OR_NONE;
                // To be strict, 'deflate' means ZLIB, but some servers were not implemented correctly.
                return EmbeddedChannel.builder()
                        .channelId(channel.id())
                        .hasDisconnect(channel.metadata().hasDisconnect())
                        .config(channel.config())
                        .handlers(ZlibCodecFactory.newZlibDecoder(wrapper, maxAllocation))
                        .build();
            }
            if (Brotli.isAvailable() && BR.contentEqualsIgnoreCase(contentEncoding)) {
                return EmbeddedChannel.builder()
                        .channelId(channel.id())
                        .hasDisconnect(channel.metadata().hasDisconnect())
                        .config(channel.config())
                        .handlers(new BrotliDecoder(maxAllocation))
                        .build();
            }
            if (SNAPPY.contentEqualsIgnoreCase(contentEncoding)) {
                return EmbeddedChannel.builder()
                        .channelId(channel.id())
                        .hasDisconnect(channel.metadata().hasDisconnect())
                        .config(channel.config())
                        .handlers(new SnappyFrameDecoder())
                        .build();
            }
            if (Zstd.isAvailable() && ZSTD.contentEqualsIgnoreCase(contentEncoding)) {
                return EmbeddedChannel.builder()
                        .channelId(channel.id())
                        .hasDisconnect(channel.metadata().hasDisconnect())
                        .config(channel.config())
                        .handlers(new ZstdDecoder(maxAllocation))
                        .build();
            }
            // 'identity' or unsupported
            return null;
        }
}
