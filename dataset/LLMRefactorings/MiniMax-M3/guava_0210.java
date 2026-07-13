public class guava_0210 {

    @Override
    public Optional<Long> sizeIfKnown() {
        if (!(sources instanceof Collection)) {
            return Optional.absent();
        }
        return sumSizes();
    }

    private Optional<Long> sumSizes() {
        long result = 0L;
        for (ByteSource source : sources) {
            Optional<Long> sizeIfKnown = source.sizeIfKnown();
            if (!sizeIfKnown.isPresent()) {
                return Optional.absent();
            }
            result += sizeIfKnown.get();
            if (result < 0) {
                return Optional.of(Long.MAX_VALUE);
            }
        }
        return Optional.of(result);
    }
}
