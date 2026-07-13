public class kafka_0260 {

    @Override
    public void close() {
        if (globalStores.isEmpty()) {
            return;
        }
        final StringBuilder closeFailed = new StringBuilder();
        for (final Map.Entry<String, Optional<StateStore>> entry : globalStores.entrySet()) {
            closeStore(entry, closeFailed);
        }

        LegacyCheckpointingStateStore.maybeDowngradeOffsets(logPrefix, upgradeFrom, stateDirectory, null, currentOffsets);

        if (closeFailed.length() > 0) {
            throw new ProcessorStateException("Exceptions caught during close of 1 or more global state globalStores\n" + closeFailed);
        }
    }

    private void closeStore(final Map.Entry<String, Optional<StateStore>> entry, final StringBuilder closeFailed) {
        if (entry.getValue().isPresent()) {
            log.debug("Closing global storage engine {}", entry.getKey());
            try {
                entry.getValue().get().close();
            } catch (final RuntimeException e) {
                log.error("Failed to close global state store {}", entry.getKey(), e);
                closeFailed.append("Failed to close global state store:")
                    .append(entry.getKey())
                    .append(". Reason: ")
                    .append(e)
                    .append("\n");
            }
            globalStores.put(entry.getKey(), Optional.empty());
        } else {
            log.info("Skipping to close non-initialized store {}", entry.getKey());
        }
    }
}
