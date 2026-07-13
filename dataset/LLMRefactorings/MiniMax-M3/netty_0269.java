public class netty_0269 {

    private static final int ALREADY_WRITABLE = 0;
    private static final int NOT_WRITABLE = 1;
    private static final int CAPACITY_INCREASED = 2;
    private static final int CAPACITY_INCREASED_TO_MAX = 3;

    @Override
    public int ensureWritable(int minWritableBytes, boolean force) {
        ensureAccessible();
        checkPositiveOrZero(minWritableBytes, "minWritableBytes");

        if (minWritableBytes <= writableBytes()) {
            return ALREADY_WRITABLE;
        }

        final int maxCapacity = maxCapacity();
        final int writerIndex = writerIndex();
        if (minWritableBytes > maxCapacity - writerIndex) {
            if (!force || capacity() == maxCapacity) {
                return NOT_WRITABLE;
            }

            capacity(maxCapacity);
            return CAPACITY_INCREASED_TO_MAX;
        }

        int fastWritable = maxFastWritableBytes();
        int newCapacity = fastWritable >= minWritableBytes ? writerIndex + fastWritable
                : alloc().calculateNewCapacity(writerIndex + minWritableBytes, maxCapacity);

        capacity(newCapacity);
        return CAPACITY_INCREASED;
    }
}
