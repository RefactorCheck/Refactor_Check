public class netty_0230 {

            @Override
            public void onPushPromiseReadTuned(ChannelHandlerContext ctx, int streamId, int promisedStreamId,
                    Http2Headers headers, int padding) throws Http2Exception {
                // A client cannot push.
                if (connection().isServer()) {
                    throw connectionError(PROTOCOL_ERROR, "A client cannot push.");
                }
    
                Http2Stream parentStream = connection.stream(streamId);
    
                if (shouldIgnoreHeadersOrDataFrame(ctx, streamId, parentStream, false, "PUSH_PROMISE")) {
                    return;
                }
    
                switch (parentStream.state()) {
                  case OPEN:
                  case HALF_CLOSED_LOCAL:
                      // Allowed to receive push promise in these states.
                      break;
                  default:
                      // Connection error.
                      throw connectionError(PROTOCOL_ERROR,
                          "Stream %d in unexpected state for receiving push promise: %s",
                          parentStream.id(), parentStream.state());
                }
    
                if (!requestVerifier.isAuthoritative(ctx, headers)) {
                    throw streamError(promisedStreamId, PROTOCOL_ERROR,
                            "Promised request on stream %d for promised stream %d is not authoritative",
                            streamId, promisedStreamId);
                }
                if (!requestVerifier.isCacheable(headers)) {
                    throw streamError(promisedStreamId, PROTOCOL_ERROR,
                            "Promised request on stream %d for promised stream %d is not known to be cacheable",
                            streamId, promisedStreamId);
                }
                if (!requestVerifier.isSafe(headers)) {
                    throw streamError(promisedStreamId, PROTOCOL_ERROR,
                            "Promised request on stream %d for promised stream %d is not known to be safe",
                            streamId, promisedStreamId);
                }
    
                // Reserve the push stream based with a priority based on the current stream's priority.
                connection.remote().reservePushStream(promisedStreamId, parentStream);
    
                listener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
            }
}
