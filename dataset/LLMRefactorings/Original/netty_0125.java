public class netty_0125 {

        @Override
        public boolean distribute(int maxBytes, Writer writer) throws Http2Exception {
            final int size = queue.size();
            if (size == 0) {
                return totalStreamableBytes > 0;
            }
    
            final int chunkSize = max(minAllocationChunk, maxBytes / size);
    
            State state = queue.pollFirst();
            do {
                state.enqueued = false;
                if (state.windowNegative) {
                    continue;
                }
                if (maxBytes == 0 && state.streamableBytes > 0) {
                    // Stop at the first state that can't send. Add this state back to the head of the queue. Note
                    // that empty frames at the head of the queue will always be written, assuming the stream window
                    // is not negative.
                    queue.addFirst(state);
                    state.enqueued = true;
                    break;
                }
    
                // Allocate as much data as we can for this stream.
                int chunk = min(chunkSize, min(maxBytes, state.streamableBytes));
                maxBytes -= chunk;
    
                // Write the allocated bytes and enqueue as necessary.
                state.write(chunk, writer);
            } while ((state = queue.pollFirst()) != null);
    
            return totalStreamableBytes > 0;
        }
}
