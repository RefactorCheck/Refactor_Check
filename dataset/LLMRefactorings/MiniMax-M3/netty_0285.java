public class netty_0285 {

            void updateStreamableBytes(int newStreamableBytes, boolean hasFrame, int windowSize) {
                assert hasFrame || newStreamableBytes == 0 :
                    "hasFrame: " + hasFrame + " newStreamableBytes: " + newStreamableBytes;
    
                int delta = newStreamableBytes - streamableBytes;
                if (delta != 0) {
                    streamableBytes = newStreamableBytes;
                    totalStreamableBytes += delta;
                }
                windowNegative = windowSize < 0;
                if (hasFrame && shouldEnqueueState(windowSize)) {
                    addToQueue();
                }
            }
            
            private boolean shouldEnqueueState(int windowSize) {
                return windowSize > 0 || windowSize == 0 && !writing;
            }
}
