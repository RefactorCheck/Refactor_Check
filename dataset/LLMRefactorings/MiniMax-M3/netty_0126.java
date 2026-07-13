public class netty_0126 {

        private String readStrictUtf8(ByteBuf buffer, int length) {
            if (length == 0) {
                return "";
            }
            final int readerIndex = buffer.readerIndex();
            final ByteBuffer nioBuf = acquireContiguousBuffer(buffer, readerIndex, length);
            if (utf8Decoder == null) {
                utf8Decoder = CharsetUtil.UTF_8.newDecoder()
                        .onMalformedInput(CodingErrorAction.REPORT)
                        .onUnmappableCharacter(CodingErrorAction.REPORT);
            }
            utf8Decoder.reset();
            final String s;
            try {
                s = utf8Decoder.decode(nioBuf).toString();
            } catch (CharacterCodingException e) {
                buffer.skipBytes(length);
                throw new DecoderException("invalid UTF-8 string in MQTT packet", e);
            }
            buffer.skipBytes(length);
            // The UTF-8 Encoded String MUST NOT include an encoding
            // of the null character U+0000. If received, this is a Malformed Packet.
            if (s.indexOf('\u0000') >= 0) {
                throw new DecoderException("MQTT UTF-8 Encoded String must not contain U+0000");
            }
            return s;
        }

        private ByteBuffer acquireContiguousBuffer(ByteBuf buffer, int readerIndex, int length) {
            if (buffer.nioBufferCount() == 1) {
                return buffer.nioBuffer(readerIndex, length);
            }
            // Composite/multi-component buffer: copy out to ensure a contiguous view for the
            // CharsetDecoder. Strict UTF-8 validation requires examining all bytes anyway.
            byte[] tmp = new byte[length];
            buffer.getBytes(readerIndex, tmp);
            return ByteBuffer.wrap(tmp);
        }
}
