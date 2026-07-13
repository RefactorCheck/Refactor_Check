public class kafka_0204 {

        public Optional<CompletedTxn> appendEndTxnMarker(EndTransactionMarker endTxnMarker,
                                                         short producerEpoch,
                                                         long offset,
                                                         long timestamp,
                                                         short transactionVersion) {
            // For replication (REPLICATION origin), TV_UNKNOWN is allowed because:
            // 1. transactionVersion is not stored in MemoryRecords - it's only metadata in WriteTxnMarkersRequest
            // 2. When records are replicated, followers only see MemoryRecords without transactionVersion
            // 3. The leader already validated the marker with the correct transactionVersion (e.g., TV2 strict validation)
            // 4. Using TV_0 validation (markerEpoch >= currentEpoch) is safe because it's more permissive than TV2
            //    (markerEpoch > currentEpoch), so any marker that passed TV2 validation will pass TV_0 validation
            // For all other origins (CLIENT, COORDINATOR), transactionVersion must be explicitly specified.
            if (transactionVersion == TV_UNKNOWN && origin != AppendOrigin.REPLICATION) {
                throw new IllegalArgumentException("transactionVersion must be explicitly specified, " +
                        "cannot use default value TV_UNKNOWN for origin " + origin);
            }
            // For replication with TV_UNKNOWN, use legacy validation (TV_0 behavior) since the leader already
            // performed strict validation and the follower doesn't have access to the original transactionVersion
            short effectiveTransactionVersion = (transactionVersion == TV_UNKNOWN) ? 0 : transactionVersion;
            checkProducerEpoch(producerEpoch, offset, effectiveTransactionVersion);
            checkCoordinatorEpoch(endTxnMarker, offset);
    
            // Only emit the `CompletedTxn` for non-empty transactions. A transaction marker
            // without any associated data will not have any impact on the last stable offset
            // and would not need to be reflected in the transaction index.
            Optional<CompletedTxn> completedTxn = updatedEntry.currentTxnFirstOffset().isPresent() ?
                    Optional.of(new CompletedTxn(producerId, updatedEntry.currentTxnFirstOffset().getAsLong(), offset,
                            endTxnMarker.controlType() == ControlRecordType.ABORT))
                    : Optional.empty();
            updatedEntry.update(producerEpoch, endTxnMarker.coordinatorEpoch(), timestamp);
            return completedTxn;
        }
}
