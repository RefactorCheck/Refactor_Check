public class netty_0002 {

            void increaseReceivedBytes(boolean server, int streamId, int bytes, boolean isEnd) throws Http2Exception {
                seen += bytes;
                // Check for overflow
                if (seen < 0) {
                    throw streamError(streamId, PROTOCOL_ERROR,
                            "Received amount of data did overflow and so not match content-length header %d", expected);
                }
                // Check if we received more data then what was advertised via the content-length header.
                if (seen > expected) {
                    throw streamError(streamId, PROTOCOL_ERROR,
                            "Received amount of data %d does not match content-length header %d", seen, expected);
                }
    
                if (isEnd) {
                    if (seen == 0 && !server) {
                        // This may be a response to a HEAD request, let's just allow it.
                        return;
                    }
    
                    // Check that we really saw what was told via the content-length header.
                    if (expected > seen) {
                        throw streamError(streamId, PROTOCOL_ERROR,
                                "Received amount of data %d does not match content-length header %d", seen, expected);
                    }
                }
            }
}
