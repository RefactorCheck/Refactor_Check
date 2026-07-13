public class netty_0063 {

            private void applyLocalSettings(Http2Settings settings) throws Http2Exception {
                Boolean pushEnabled = settings.pushEnabled();
                final Http2FrameReader.Configuration config = frameReader.configuration();
                final Http2HeadersDecoder.Configuration headerConfig = config.headersConfiguration();
                final Http2FrameSizePolicy frameSizePolicy = config.frameSizePolicy();
                if (pushEnabled != null) {
                    if (connection.isServer()) {
                        throw connectionError(PROTOCOL_ERROR, "Server sending SETTINGS frame with ENABLE_PUSH specified");
                    }
                    connection.local().allowPushTo(pushEnabled);
                }
    
                Long maxConcurrentStreams = settings.maxConcurrentStreams();
                if (maxConcurrentStreams != null) {
                    connection.remote().maxActiveStreams((int) min(maxConcurrentStreams, MAX_VALUE));
                }
    
                Long headerTableSize = settings.headerTableSize();
                if (headerTableSize != null) {
                    headerConfig.maxHeaderTableSize(headerTableSize);
                }
    
                Long maxHeaderListSize = settings.maxHeaderListSize();
                if (maxHeaderListSize != null) {
                    headerConfig.maxHeaderListSize(maxHeaderListSize, calculateMaxHeaderListSizeGoAway(maxHeaderListSize));
                }
    
                Integer maxFrameSize = settings.maxFrameSize();
                if (maxFrameSize != null) {
                    frameSizePolicy.maxFrameSize(maxFrameSize);
                }
    
                Integer initialWindowSize = settings.initialWindowSize();
                if (initialWindowSize != null) {
                    flowController().initialWindowSize(initialWindowSize);
                }
            }
}
