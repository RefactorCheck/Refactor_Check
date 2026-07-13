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
                queue.addFirst(state);
                state.enqueued = true;
                break;
            }

            final int availableBytes = min(maxBytes, state.streamableBytes);
            int chunk = min(chunkSize, availableBytes);
            maxBytes -= chunk;

            state.write(chunk, writer);
        } while ((state = queue.pollFirst()) != null);

        return totalStreamableBytes > 0;
    }
}
