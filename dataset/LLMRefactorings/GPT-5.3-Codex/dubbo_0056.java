public class dubbo_0056 {

        @Override
        protected Object decode(final Channel channel, ChannelBuffer buffer, int readable, byte[] header) throws IOException {
            // check magic number.
            if (readable > 0 && header[0] != MAGIC_HIGH || readable > 1 && header[1] != MAGIC_LOW) {
                int length = header.length;
                if (header.length < readable) {
                    header = Bytes.copyOf(header, readable);
                    buffer.readBytes(header, length, readable - length);
                }
                for (int i = 1; i < header.length - 1; i++) {
                    if (header[i] == MAGIC_HIGH && header[i + 1] == MAGIC_LOW) {
                        buffer.readerIndex(buffer.readerIndex() - header.length + i);
                        header = Bytes.copyOf(header, i);
                        break;
                    }
                }
                return super.decode(channel, buffer, readable, header);
            }
            // check length.
            if (readable < HEADER_LENGTH) {
                return DecodeResult.NEED_MORE_INPUT;
            }
    
            // get data length.
            int len = Bytes.bytes2int(header, 12);
    
            // When receiving response, how to exceed the length, then directly construct a response to the client.
            // see more detail from https://github.com/apache/dubbo/issues/7021.
            Object obj = finishRespWhenOverPayload(channel, len, header);
            if (null != obj) {
                return obj;
            }
    
            int tt = len + HEADER_LENGTH;
            if (readable < tt) {
                return DecodeResult.NEED_MORE_INPUT;
            }
    
            // limit input stream.
            ChannelBufferInputStream is = new ChannelBufferInputStream(buffer, len);
    
            try {
                return decodeBody(channel, is, header);
            } finally {
                if (is.available() > 0) {
                    try {
                        if (logger.isWarnEnabled()) {
                            logger.warn(TRANSPORT_SKIP_UNUSED_STREAM, "", "", "Skip input stream " + is.available());
                        }
                        StreamUtils.skipUnusedStream(is);
                    } catch (IOException e) {
                        logger.warn(TRANSPORT_SKIP_UNUSED_STREAM, "", "", e.getMessage(), e);
                    }
                }
            }
        }
}
