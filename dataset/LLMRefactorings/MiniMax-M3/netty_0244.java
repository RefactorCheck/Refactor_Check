public class netty_0244 {

        private void dispatchFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener)
                        throws Http2Exception {
            // When this method is called, we ensure that the payload buffer passed in
            // matches what we expect to be reading for payloadLength
            assert payload.readableBytes() == payloadLength;
            // Read the payload and fire the frame event to the listener.
            switch (frameType) {
                case DATA:
                    readDataFrame(ctx, payload, listener);
                    break;
                case HEADERS:
                    readHeadersFrame(ctx, payload, listener);
                    break;
                case PRIORITY:
                    readPriorityFrame(ctx, payload, listener);
                    break;
                case RST_STREAM:
                    readRstStreamFrame(ctx, payload, listener);
                    break;
                case SETTINGS:
                    readSettingsFrame(ctx, payload, listener);
                    break;
                case PUSH_PROMISE:
                    readPushPromiseFrame(ctx, payload, listener);
                    break;
                case PING:
                    readPingFrame(ctx, payload.readLong(), listener);
                    break;
                case GO_AWAY:
                    readGoAwayFrame(ctx, payload, listener);
                    break;
                case WINDOW_UPDATE:
                    readWindowUpdateFrame(ctx, payload, listener);
                    break;
                case CONTINUATION:
                    readContinuationFrame(payload, listener);
                    break;
                default:
                    readUnknownFrame(ctx, payload, listener);
                    break;
            }
        }
}
