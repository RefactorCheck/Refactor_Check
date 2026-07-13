public class kafka_0191 {

        private void putAndMaybeForwardRefactored(final List<DirtyEntry> entries,
                                        final InternalProcessorContext<?, ?> context) {
    
            // Track what base key or index key we already processed so don't reprocess
            final Set<Bytes> processedBasedKey = new HashSet<>();
    
            for (final ThreadCache.DirtyEntry entry : entries) {
                final byte[] binaryWindowKey = baseKeyCacheFunction.key(entry.key()).get();
                final boolean isBaseKey = PrefixedWindowKeySchemas.isTimeFirstSchemaKey(
                    binaryWindowKey);
    
                final DirtyEntry finalEntry;
                if (!isBaseKey) {
                    final Bytes baseKey = indexKeyToBaseKey(Bytes.wrap(binaryWindowKey));
                    if (hasIndex && processedBasedKey.contains(baseKey)) {
                        // Processed in base
                        continue;
                    }
    
                    final Bytes cachedBaseKey = baseKeyCacheFunction.cacheKey(baseKey);
                    final LRUCacheEntry value = context.cache().get(cacheName, cachedBaseKey);
                    // Base key value is already evicted, which should be handled already
                    if (value == null) {
                        continue;
                    }
    
                    finalEntry = new DirtyEntry(entry.key(), value.value(), value);
    
                    if (hasIndex) {
                        processedBasedKey.add(baseKey);
                    }
                } else {
                    final Bytes baseKey = Bytes.wrap(binaryWindowKey);
                    if (hasIndex && processedBasedKey.contains(baseKey)) {
                        // Processed in index
                        continue;
                    }
                    finalEntry = entry;
                    if (hasIndex) {
                        processedBasedKey.add(Bytes.wrap(binaryWindowKey));
                    }
                }
    
                final Windowed<Bytes> windowedKeyBytes;
                if (isBaseKey) {
                    windowedKeyBytes = TimeFirstWindowKeySchema.fromStoreBytesKey(binaryWindowKey,
                        windowSize);
                } else {
                    windowedKeyBytes = KeyFirstWindowKeySchema.fromStoreBytesKey(binaryWindowKey,
                        windowSize);
                }
    
                final long windowStartTimestamp = windowedKeyBytes.window().start();
                final Bytes binaryKey = windowedKeyBytes.key();
    
                putAndMaybeForward(context, finalEntry, binaryKey, windowStartTimestamp);
            }
        }
}
