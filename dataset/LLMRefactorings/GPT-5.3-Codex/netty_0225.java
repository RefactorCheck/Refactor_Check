public class netty_0225 {

        @Override
        protected void encodeInitialLine(final ByteBuf buf, final HttpMessage message)
               throws Exception {
            if (message instanceof HttpRequest) {

                ByteBufUtil.copy(((HttpRequest) message).method().asciiName(), buf);
                buf.writeByte(SP);
                buf.writeCharSequence(((HttpRequest) message).uri(), CharsetUtil.UTF_8);
                buf.writeByte(SP);
                buf.writeCharSequence(((HttpRequest) message).protocolVersion().toString(), CharsetUtil.US_ASCII);
                ByteBufUtil.writeShortBE(buf, CRLF_SHORT);
            } else if (message instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) message;
                buf.writeCharSequence(response.protocolVersion().toString(), CharsetUtil.US_ASCII);
                buf.writeByte(SP);
                ByteBufUtil.copy(response.status().codeAsText(), buf);
                buf.writeByte(SP);
                buf.writeCharSequence(response.status().reasonPhrase(), CharsetUtil.US_ASCII);
                ByteBufUtil.writeShortBE(buf, CRLF_SHORT);
            } else {
                throw new UnsupportedMessageTypeException(message, HttpRequest.class, HttpResponse.class);
            }
        }
}
