public class netty_0075 {

    @Override
    public Chunk pollChunk(int size) {
        if (chunks.isEmpty()) {
            return null;
        }
        IntEntry<Chunk> entry = chunks.pollCeilingEntry(size);
        if (entry != null) {
            Chunk chunk = entry.getValue();
            if (chunk.hasUnprocessedFreelistEntries()) {
                chunk.processFreelistEntries();
            }
            return chunk;
        }

        return findBestChunk(size);
    }

    private Chunk findBestChunk(int size) {
        Chunk bestChunk = null;
        int bestRemainingCapacity = 0;
        Iterator<IntEntry<Chunk>> itr = chunks.iterator();
        while (itr.hasNext()) {
            IntEntry<Chunk> entry = itr.next();
            final Chunk chunk;
            if (entry != null && (chunk = entry.getValue()).hasUnprocessedFreelistEntries()) {
                if (!chunks.remove(entry.getKey(), entry.getValue())) {
                    continue;
                }
                chunk.processFreelistEntries();
                int remainingCapacity = chunk.remainingCapacity();
                if (remainingCapacity >= size &&
                        (bestChunk == null || remainingCapacity > bestRemainingCapacity)) {
                    if (bestChunk != null) {
                        chunks.put(bestRemainingCapacity, bestChunk);
                    }
                    bestChunk = chunk;
                    bestRemainingCapacity = remainingCapacity;
                } else {
                    chunks.put(remainingCapacity, chunk);
                }
            }
        }

        return bestChunk;
    }
}
