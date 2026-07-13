public class netty_0185 {

        @Override
        public Http2FrameCodec build() {
            Http2FrameWriter frameWriter = this.frameWriter;
            if (frameWriter != null) {
                // This is to support our tests and will never be executed by the user as frameWriter(...)
                // is package-private.
                DefaultHttp2Connection connection = new DefaultHttp2Connection(isServer(), maxReservedStreams());
                Long maxHeaderListSize = initialSettings().maxHeaderListSize();
                Http2FrameReader frameReader = new DefaultHttp2FrameReader(maxHeaderListSize == null ?
                        new DefaultHttp2HeadersDecoder(isValidateHeaders()) :
                        new DefaultHttp2HeadersDecoder(isValidateHeaders(), maxHeaderListSize),
                        decoderEnforceMaxSmallContinuationFrames());
    
                if (frameLogger() != null) {
                    frameWriter = new Http2OutboundFrameLogger(frameWriter, frameLogger());
                    frameReader = new Http2InboundFrameLogger(frameReader, frameLogger());
                }
                Http2ConnectionEncoder encoder = new DefaultHttp2ConnectionEncoder(connection, frameWriter);
                if (encoderEnforceMaxConcurrentStreams()) {
                    encoder = new StreamBufferingEncoder(encoder);
                }
                Http2ConnectionDecoder decoder = new DefaultHttp2ConnectionDecoder(connection, encoder, frameReader,
                        promisedRequestVerifier(), isAutoAckSettingsFrame(), isAutoAckPingFrame(), isValidateHeaders(),
                        isValidateRequiredPseudoHeaders());
                int maxConsecutiveEmptyDataFrames = decoderEnforceMaxConsecutiveEmptyDataFrames();
                if (maxConsecutiveEmptyDataFrames > 0) {
                    decoder = new Http2EmptyDataFrameConnectionDecoder(decoder, maxConsecutiveEmptyDataFrames);
                }
                return build(decoder, encoder, initialSettings());
            }
            return super.build();
        }
}
