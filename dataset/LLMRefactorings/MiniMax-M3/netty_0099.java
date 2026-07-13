public class netty_0099 {

    private static final int HOST_LENGTH = 4;
    private static final int ACCEPT_LENGTH = 6;
    private static final int CONNECTION_LENGTH = 10;
    private static final int CONTENT_TYPE_LENGTH = 12;
    private static final int CONTENT_LENGTH_LENGTH = 14;

    @Override
    protected AsciiString splitHeaderName(final byte[] sb, final int start, final int length) {
        final byte firstChar = sb[start];
        if (firstChar == 'H') {
            if (length == HOST_LENGTH && isHost(sb, start)) {
                return Host;
            }
        } else if (firstChar == 'A') {
            if (length == ACCEPT_LENGTH && isAccept(sb, start)) {
                return Accept;
            }
        } else if (firstChar == 'C') {
            if (length == CONNECTION_LENGTH) {
                if (isConnection(sb, start)) {
                    return Connection;
                }
            } else if (length == CONTENT_TYPE_LENGTH) {
                if (isContentType(sb, start)) {
                    return ContentType;
                }
            } else if (length == CONTENT_LENGTH_LENGTH) {
                if (isContentLength(sb, start)) {
                    return ContentLength;
                }
            }
        }
        return super.splitHeaderName(sb, start, length);
    }
}
