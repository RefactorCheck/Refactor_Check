public class netty_0193 {

    private static final int INITIAL_DETAILS_CAPACITY = 4;

    @Override
    protected SmtpResponse decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
        if (frame == null) {
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
                if (detail != null) {
                    if (details == null) {
                        this.details = details = new ArrayList<CharSequence>(INITIAL_DETAILS_CAPACITY);
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
