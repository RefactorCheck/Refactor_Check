public class netty_0244 {

        private void processPayloadState(ChannelHandlerContext ctx, ByteBuf in, Http2FrameListener listener)
                        throws Http2Exception {
            // When this method is called, we ensure that the payload buffer passed in
            // matches what we expect to be reading for payloadLength
            assert in.readableBytes() == payloadLength;
            // Read the payload and fire the frame event to the listener.
            switch (frameType) {
                case DATA:
                    readDataFrame(ctx, in, listener);
                    break;
                case HEADERS:
                    readHeadersFrame(ctx, in, listener);
                    break;
                case PRIORITY:
                    readPriorityFrame(ctx, in, listener);
                    break;
                case RST_STREAM:
                    readRstStreamFrame(ctx, in, listener);
                    break;
                case SETTINGS:
                    readSettingsFrame(ctx, in, listener);
                    break;
                case PUSH_PROMISE:
                    readPushPromiseFrame(ctx, in, listener);
                    break;
                case PING:
                    readPingFrame(ctx, in.readLong(), listener);
                    break;
                case GO_AWAY:
                    readGoAwayFrame(ctx, in, listener);
                    break;
                case WINDOW_UPDATE:
                    readWindowUpdateFrame(ctx, in, listener);
                    break;
                case CONTINUATION:
                    readContinuationFrame(in, listener);
                    break;
                default:
                    readUnknownFrame(ctx, in, listener);
                    break;
            }
        }
}
