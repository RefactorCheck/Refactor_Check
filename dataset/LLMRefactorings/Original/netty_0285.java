public class netty_0285 {

            void updateStreamableBytes(int newStreamableBytes, boolean hasFrame, int windowSize) {
                assert hasFrame || newStreamableBytes == 0 :
                    "hasFrame: " + hasFrame + " newStreamableBytes: " + newStreamableBytes;
    
                int delta = newStreamableBytes - streamableBytes;
                if (delta != 0) {
                    streamableBytes = newStreamableBytes;
                    totalStreamableBytes += delta;
                }
                // In addition to only enqueuing state when they have frames we enforce the following restrictions:
                // 1. If the window has gone negative. We never want to queue a state. However we also don't want to
                //    Immediately remove the item if it is already queued because removal from deque is O(n). So
                //    we allow it to stay queued and rely on the distribution loop to remove this state.
                // 2. If the window is zero we only want to queue if we are not writing. If we are writing that means
                //    we gave the state a chance to write zero length frames. We wait until updateStreamableBytes is
                //    called again before this state is allowed to write.
                windowNegative = windowSize < 0;
                if (hasFrame && (windowSize > 0 || windowSize == 0 && !writing)) {
                    addToQueue();
                }
            }
}
