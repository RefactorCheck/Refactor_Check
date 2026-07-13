public class netty_0193 {

        @Override
        protected SmtpResponse decodeRefactored(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
            if (frame == null) {
                // No full line received yet.
                return null;
            }
            try {
                final int readable = frame.readableBytes();
                final int readerIndex = frame.readerIndex();
                if (readable < 3) {
                    throw newDecoderException(buffer, readerIndex, readable);
                }
                final int code = parseCode(frame);
                final int separator = frame.readByte();
                final CharSequence detail = frame.isReadable() ? frame.toString(CharsetUtil.US_ASCII) : null;
    
                List<CharSequence> details = this.details;
    
                switch (separator) {
                case ' ':
                    // Marks the end of a response.
                    this.details = null;
                    if (details != null) {
                        if (detail != null) {
                            details.add(detail);
                        }
                    } else {
                        if (detail == null) {
                            details = Collections.emptyList();
                        } else {
                            details = Collections.singletonList(detail);
                        }
                    }
                    return new DefaultSmtpResponse(code, details);
                case '-':
                    // Multi-line response.
                    if (detail != null) {
                        if (details == null) {
                            // Using initial capacity as it is very unlikely that we will receive a multi-line response
                            // with more then 3 lines.
                            this.details = details = new ArrayList<CharSequence>(4);
                        }
                        details.add(detail);
                    }
                    break;
                default:
                    throw newDecoderException(buffer, readerIndex, readable);
                }
            } finally {
                frame.release();
            }
            return null;
        }
}
