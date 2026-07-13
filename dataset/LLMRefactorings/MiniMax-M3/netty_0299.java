public class netty_0299 {

        private void verifyFrame() throws Http2Exception {
            switch (frameType) {
                case DATA:
                    verifyDataFrame();
                    break;
                case HEADERS:
                    verifyHeadersFrame();
                    break;
                case PRIORITY:
                    verifyPriorityFrame();
                    break;
                case RST_STREAM:
                    verifyRstStreamFrame();
                    break;
                case SETTINGS:
                    verifySettingsFrame();
                    break;
                case PUSH_PROMISE:
                    verifyPushPromiseFrame();
                    break;
                case PING:
                    verifyPingFrame();
                    break;
                case GO_AWAY:
                    verifyGoAwayFrame();
                    break;
                case WINDOW_UPDATE:
                    verifyWindowUpdateFrame();
                    break;
                case CONTINUATION:
                    verifyContinuationFrame();
                    break;
                default:
                    verifyUnknownFrame();
                    break;
            }
        }
}
