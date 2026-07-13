public class kafka_0191 {

        private void putAndMaybeForward(final List<DirtyEntry> entries,
                                        final InternalProcessorContext<?, ?> context) {
    
            final Set<Bytes> processedBaseKey = new HashSet<>();
    
            for (final ThreadCache.DirtyEntry entry : entries) {
                final byte[] binaryWindowKey = baseKeyCacheFunction.key(entry.key()).get();
                final boolean isBaseKey = PrefixedWindowKeySchemas.isTimeFirstSchemaKey(
                    binaryWindowKey);
    
                final DirtyEntry finalEntry;
                if (!isBaseKey) {
                    final Bytes baseKey = indexKeyToBaseKey(Bytes.wrap(binaryWindowKey));
                    if (hasIndex && processedBaseKey.contains(baseKey)) {
                        continue;
                    }
    
                    final Bytes cachedBaseKey = baseKeyCacheFunction.cacheKey(baseKey);
                    final LRUCacheEntry value = context.cache().get(cacheName, cachedBaseKey);
                    if (value == null) {
                        continue;
                    }
    
                    finalEntry = new DirtyEntry(entry.key(), value.value(), value);
    
                    if (hasIndex) {
                        processedBaseKey.add(baseKey);
                    }
                } else {
                    final Bytes baseKey = Bytes.wrap(binaryWindowKey);
                    if (hasIndex && processedBaseKey.contains(baseKey)) {
                        continue;
                    }
                    finalEntry = entry;
                    if (hasIndex) {
                        processedBaseKey.add(Bytes.wrap(binaryWindowKey));
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
