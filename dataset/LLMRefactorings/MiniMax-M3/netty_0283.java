public class netty_0283 {

    private EmbeddedChannel buildDecompressorChannel(Channel channel, ChannelHandler... handlers) {
        return EmbeddedChannel.builder()
                .channelId(channel.id())
                .hasDisconnect(channel.metadata().hasDisconnect())
                .config(channel.config())
                .handlers(handlers)
                .build();
    }

    protected EmbeddedChannel newContentDecompressor(final ChannelHandlerContext ctx, CharSequence contentEncoding)
            throws Http2Exception {
        Channel channel = ctx.channel();
        if (GZIP.contentEqualsIgnoreCase(contentEncoding) || X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
            return buildDecompressorChannel(channel, ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP, maxAllocation));
        }
        if (DEFLATE.contentEqualsIgnoreCase(contentEncoding) || X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
            final ZlibWrapper wrapper = strict ? ZlibWrapper.ZLIB : ZlibWrapper.ZLIB_OR_NONE;
            // To be strict, 'deflate' means ZLIB, but some servers were not implemented correctly.
            return buildDecompressorChannel(channel, ZlibCodecFactory.newZlibDecoder(wrapper, maxAllocation));
        }
        if (Brotli.isAvailable() && BR.contentEqualsIgnoreCase(contentEncoding)) {
            return buildDecompressorChannel(channel, new BrotliDecoder(maxAllocation));
        }
        if (SNAPPY.contentEqualsIgnoreCase(contentEncoding)) {
            return buildDecompressorChannel(channel, new SnappyFrameDecoder());
        }
        if (Zstd.isAvailable() && ZSTD.contentEqualsIgnoreCase(contentEncoding)) {
            return buildDecompressorChannel(channel, new ZstdDecoder(maxAllocation));
        }
        // 'identity' or unsupported
        return null;
    }
}
