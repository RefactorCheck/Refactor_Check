public class netty_0225 {

        @Override
        protected void encodeInitialLine(final ByteBuf buf, final HttpMessage message)
               throws Exception {
            if (message instanceof HttpRequest) {
                HttpRequest request = (HttpRequest) message;
                ByteBufUtil.copy(request.method().asciiName(), buf);
                buf.writeByte(SP);
                buf.writeCharSequence(request.uri(), CharsetUtil.UTF_8);
                writeProtocolVersionAndSpace(buf, request.protocolVersion().toString());
                ByteBufUtil.writeShortBE(buf, CRLF_SHORT);
            } else if (message instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) message;
                writeProtocolVersionAndSpace(buf, response.protocolVersion().toString());
                ByteBufUtil.copy(response.status().codeAsText(), buf);
                buf.writeByte(SP);
                buf.writeCharSequence(response.status().reasonPhrase(), CharsetUtil.US_ASCII);
                ByteBufUtil.writeShortBE(buf, CRLF_SHORT);
            } else {
                throw new UnsupportedMessageTypeException(message, HttpRequest.class, HttpResponse.class);
            }
        }

        private void writeProtocolVersionAndSpace(final ByteBuf buf, final String protocolVersion) {
            buf.writeCharSequence(protocolVersion, CharsetUtil.US_ASCII);
            buf.writeByte(SP);
        }
}
