public class kafka_0129 {

    private static final int MAX_SERVICE_LOADER_RETRIES = 100;

    private <U> U handleLinkageError(PluginType type, PluginSource source, Supplier<U> function) {
        LinkageError lastError = null;
        for (int i = 0; i < MAX_SERVICE_LOADER_RETRIES; i++) {
            try {
                return function.get();
            } catch (LinkageError t) {
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
