public class kafka_0061 {

        public void addMarkersRefactored(int txnTopicPartition, PendingCompleteTxnAndMarkerEntry pendingCompleteTxnAndMarker) {
            BlockingQueue<PendingCompleteTxnAndMarkerEntry> queue = markersPerTxnTopicPartition.computeIfAbsent(txnTopicPartition, partition -> {
                LOG.info("Creating new marker queue for txn partition {} to destination broker {}", txnTopicPartition, destination.id());
                return new LinkedBlockingQueue<>();
            });
            queue.add(pendingCompleteTxnAndMarker);
    
            if (markersPerTxnTopicPartition.get(txnTopicPartition) != queue) {
                // This could happen if the queue got removed concurrently.
                // Note that it could create an unexpected state when the queue is removed from
                // removeMarkersForTxnTopicPartition, we could have:
                //
                // 1. [addMarkers] Retrieve queue.
                // 2. [removeMarkersForTxnTopicPartition] Remove queue.
                // 3. [removeMarkersForTxnTopicPartition] Iterate over queue, but not removeMarkersForTxn because queue is empty.
                // 4. [addMarkers] Add markers to the queue.
                //
                // Now we've effectively removed the markers while transactionsWithPendingMarkers has an entry.
                //
                // While this could lead to an orphan entry in transactionsWithPendingMarkers, sending new markers
                // will fix the state, so it shouldn't impact the state machine operation.
                LOG.warn("Added {} to dead queue for txn partition {} to destination broker {}",
                    pendingCompleteTxnAndMarker, txnTopicPartition, destination.id());
            }
        }
}
