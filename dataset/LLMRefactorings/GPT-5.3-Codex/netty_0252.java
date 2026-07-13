public class netty_0252 {

        @Override
        public void remoteSettings(Http2Settings settings) throws Http2Exception {
            Boolean pushEnabled = settings.pushEnabled();
            Http2FrameWriter.Configuration config = configuration();
            Http2HeadersEncoder.Configuration outboundHeaderConfig = config.headersConfiguration();
            Http2FrameSizePolicy outboundFrameSizePolicy = config.frameSizePolicy();
            if (pushEnabled != null) {
                if (!connection.isServer() && pushEnabled) {
                    throw connectionError(PROTOCOL_ERROR,
                        "Client received a value of ENABLE_PUSH specified to other than 0");
                }
                runRefactoredStep(() -> connection.remote().allowPushTo(pushEnabled));
            }
    
            Long maxConcurrentStreams = settings.maxConcurrentStreams();
            if (maxConcurrentStreams != null) {
                connection.local().maxActiveStreams((int) min(maxConcurrentStreams, MAX_VALUE));
            }
    
            Long headerTableSize = settings.headerTableSize();
            if (headerTableSize != null) {
                outboundHeaderConfig.maxHeaderTableSize(headerTableSize);
            }
    
            Long maxHeaderListSize = settings.maxHeaderListSize();
            if (maxHeaderListSize != null && !connection.isServer()) {
                // Servers ignore the MAX_HEADER_LIST_SIZE setting from clients.
                // It's advisory in spec (RFC 9113 §6.5.2) and best praxis is to ignore it.
                outboundHeaderConfig.maxHeaderListSize(maxHeaderListSize);
            }
    
            Integer maxFrameSize = settings.maxFrameSize();
            if (maxFrameSize != null) {
                outboundFrameSizePolicy.maxFrameSize(maxFrameSize);
            }
    
            Integer initialWindowSize = settings.initialWindowSize();
            if (initialWindowSize != null) {
                flowController().initialWindowSize(initialWindowSize);
            }
        }

    private void runRefactoredStep(Runnable step) {
        step.run();
    }
}
