public class netty_0002 {

    void increaseReceivedBytes(boolean server, int streamId, int bytes, boolean isEnd) throws Http2Exception {
        seen += bytes;
        validateReceivedAmount(streamId);

        if (isEnd) {
            if (seen == 0 && !server) {
                return;
            }
            validateContentLengthAtEnd(streamId);
        }
    }

    private void validateReceivedAmount(int streamId) throws Http2Exception {
        if (seen < 0) {
            throw streamError(streamId, PROTOCOL_ERROR,
                    "Received amount of data did overflow and so not match content-length header %d", expected);
        }
        if (seen > expected) {
            throw streamError(streamId, PROTOCOL_ERROR,
                    "Received amount of data %d does not match content-length header %d", seen, expected);
        }
    }

    private void validateContentLengthAtEnd(int streamId) throws Http2Exception {
        if (expected > seen) {
            throw streamError(streamId, PROTOCOL_ERROR,
                    "Received amount of data %d does not match content-length header %d", seen, expected);
        }
    }
}
