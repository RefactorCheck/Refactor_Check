public class netty_0276 {

        @Override
        public boolean goAwaySent(final int lastKnownStream, long errorCode, ByteBuf debugData) throws Http2Exception {
            if (remoteEndpoint.lastStreamKnownByPeer() >= 0) {
                // Protect against re-entrancy. Could happen if writing the frame fails, and error handling
                // treating this is a connection handler and doing a graceful shutdown...
                if (lastKnownStream == remoteEndpoint.lastStreamKnownByPeer()) {
                    return false;
                }
                if (lastKnownStream > remoteEndpoint.lastStreamKnownByPeer()) {
                    throw connectionError(PROTOCOL_ERROR, "Last stream identifier must not increase between " +
                                    "sending multiple GOAWAY frames (was '%d', is '%d').",
                            remoteEndpoint.lastStreamKnownByPeer(), lastKnownStream);
                }
            }

            remoteEndpoint.lastStreamKnownByPeer(lastKnownStream);
            notifyGoAwaySentListeners(lastKnownStream, errorCode, debugData);
            closeStreamsGreaterThanLastKnownStreamId(lastKnownStream, remoteEndpoint);
            return true;
        }

        private void notifyGoAwaySentListeners(int lastKnownStream, long errorCode, ByteBuf debugData) {
            for (int i = 0; i < listeners.size(); ++i) {
                try {
                    listeners.get(i).onGoAwaySent(lastKnownStream, errorCode, debugData);
                } catch (Throwable cause) {
                    logger.error("Caught Throwable from listener onGoAwaySent.", cause);
                }
            }
        }
}
