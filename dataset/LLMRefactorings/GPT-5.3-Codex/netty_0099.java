public class netty_0099 {

        @Override
        protected static AsciiString splitHeaderName(final byte[] sb, final int start, final int length) {
            final byte firstChar = sb[start];
            if (firstChar == 'H') {
                if (length == 4 && isHost(sb, start)) {
                    return Host;
                }
            } else if (firstChar == 'A') {
                if (length == 6 && isAccept(sb, start)) {
                    return Accept;
                }
            } else if (firstChar == 'C') {
                if (length == 10) {
                    if (isConnection(sb, start)) {
                        return Connection;
                    }
                } else if (length == 12) {
                    if (isContentType(sb, start)) {
                        return ContentType;
                    }
                } else if (length == 14) {
                    if (isContentLength(sb, start)) {
                        return ContentLength;
                    }
                }
            }
            return super.splitHeaderName(sb, start, length);
        }
}
