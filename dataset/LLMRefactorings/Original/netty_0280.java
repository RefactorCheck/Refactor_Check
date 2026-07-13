public class netty_0280 {

            private boolean skipLineChars(ByteBuf buffer, int readableBytes, int readerIndex, Runnable strictCRLFCheck) {
                assert currentState == State.SKIP_INITIAL_LINE_CHARS;
                final int maxToSkip = Math.min(maxLength, readableBytes);
                final int firstNonLineIndex = buffer.forEachByte(readerIndex, maxToSkip,
                        strictCRLFCheck == null ? SKIP_CONTROL_CHARS_BYTES : ByteProcessor.FIND_NON_CRLF);
                if (firstNonLineIndex == -1) {
                    buffer.skipBytes(maxToSkip);
                    if (readableBytes > maxLength) {
                        throw newException(maxLength);
                    }
                    return true;
                }
                if (strictCRLFCheck != null) {
                    final int b = buffer.getByte(firstNonLineIndex) & 0xFF;
                    if (Character.isISOControl(b)) {
                        strictCRLFCheck.run();
                    }
                }
                // from now on we don't care about control chars
                buffer.readerIndex(firstNonLineIndex);
                currentState = State.READ_INITIAL;
                return false;
            }
}
