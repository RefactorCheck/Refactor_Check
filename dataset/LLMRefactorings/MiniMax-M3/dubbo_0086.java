public class dubbo_0086 {

    public TripleConfig build() {
        TripleConfig triple = new TripleConfig();

        applyIfNotNull(maxBodySize, triple::setMaxBodySize);
        applyIfNotNull(maxResponseBodySize, triple::setMaxResponseBodySize);
        applyIfNotNull(maxChunkSize, triple::setMaxChunkSize);
        applyIfNotNull(maxHeaderSize, triple::setMaxHeaderSize);
        applyIfNotNull(maxInitialLineLength, triple::setMaxInitialLineLength);
        applyIfNotNull(initialBufferSize, triple::setInitialBufferSize);
        applyIfNotNull(headerTableSize, triple::setHeaderTableSize);
        applyIfNotNull(enablePush, triple::setEnablePush);
        applyIfNotNull(maxConcurrentStreams, triple::setMaxConcurrentStreams);
        applyIfNotNull(initialWindowSize, triple::setInitialWindowSize);
        applyIfNotNull(connectionInitialWindowSize, triple::setConnectionInitialWindowSize);
        applyIfNotNull(maxFrameSize, triple::setMaxFrameSize);
        applyIfNotNull(maxHeaderListSize, triple::setMaxHeaderListSize);

        return triple;
    }

    private static <T> void applyIfNotNull(T value, java.util.function.Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
