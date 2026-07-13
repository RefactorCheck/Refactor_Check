public class netty_0280 {

            private boolean skipLineChars(ByteBuf bufferValue, int readableBytes, int readerIndex, Runnable strictCRLFCheck) {
                assert currentState == State.SKIP_INITIAL_LINE_CHARS;
                final int maxToSkip = Math.min(maxLength, readableBytes);
                final int firstNonLineIndex = bufferValue.forEachByte(readerIndex, maxToSkip,
                        strictCRLFCheck == null ? SKIP_CONTROL_CHARS_BYTES : ByteProcessor.FIND_NON_CRLF);
                if (firstNonLineIndex == -1) {
                    bufferValue.skipBytes(maxToSkip);
                    if (readableBytes > maxLength) {
                        throw newException(maxLength);
                    }
                    return true;
                }
                if (strictCRLFCheck != null) {
                    final int b = bufferValue.getByte(firstNonLineIndex) & 0xFF;
                    if (Character.isISOControl(b)) {
                        strictCRLFCheck.run();
                    }
                }
                // from now on we don't care about control chars
                bufferValue.readerIndex(firstNonLineIndex);
                currentState = State.READ_INITIAL;
                return false;
            }
}
