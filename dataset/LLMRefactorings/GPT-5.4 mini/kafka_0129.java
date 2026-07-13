public class kafka_0129 {

        private <U> U retryLinkageErrorResolution(PluginType type, PluginSource source, Supplier<U> function) {
            // It's difficult to know for sure if the iterator was able to advance past the first broken
            // plugin class, or if it will continue to fail on that broken class for any subsequent calls
            // to Iterator::hasNext or Iterator::next
            // For reference, see https://bugs.openjdk.org/browse/JDK-8196182, which describes
            // the behavior we are trying to mitigate with this logic as buggy, but indicates that a fix
            // in the JDK standard library ServiceLoader implementation is unlikely to land
            LinkageError lastError = null;
            // Try a fixed maximum number of times in case the ServiceLoader cannot move past a faulty plugin,
            // but the LinkageError varies between calls. This limit is chosen to be higher than the typical number
            // of plugins in a single plugin location, and to limit the amount of log-spam on startup.
            for (int i = 0; i < 100; i++) {
                try {
                    return function.get();
                } catch (LinkageError t) {
                    // As an optimization, hide subsequent error logs if two consecutive errors look similar.
                    // This reduces log-spam for iterators which cannot advance and rethrow the same exception.
                    if (lastError == null
                            || !Objects.equals(lastError.getClass(), t.getClass())
                            || !Objects.equals(lastError.getMessage(), t.getMessage())) {
                        log.error("Failed to discover {} in {}{}",
                                type.simpleName(), source, reflectiveErrorDescription(t.getCause()), t);
                    }
                    lastError = t;
                }
            }
            log.error("Received excessive ServiceLoader errors: assuming the runtime ServiceLoader implementation cannot " +
                            "skip faulty implementations. Use a different JRE, or resolve LinkageErrors for plugins in {}",
                    source, lastError);
            throw lastError;
        }
}
