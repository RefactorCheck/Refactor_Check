public class netty_0270 {

    private static final long L_ECUYER_MULTIPLIER = 181783497276652981L;

    private static long newSeed() {
        for (;;) {
            final long current = seedUniquifier.get();
            final long actualCurrent = current != 0? current : getInitialSeedUniquifier();

            // L'Ecuyer, "Tables of Linear Congruential Generators of Different Sizes and Good Lattice Structure", 1999
            final long next = actualCurrent * L_ECUYER_MULTIPLIER;

            if (seedUniquifier.compareAndSet(current, next)) {
                if (current == 0 && logger.isDebugEnabled()) {
                    if (seedGeneratorEndTime != 0) {
                        logger.debug(String.format(
                                "-Dio.netty.initialSeedUniquifier: 0x%016x (took %d ms)",
                                actualCurrent,
                                TimeUnit.NANOSECONDS.toMillis(seedGeneratorEndTime - seedGeneratorStartTime)));
                    } else {
                        logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x", actualCurrent));
                    }
                }
                return next ^ System.nanoTime();
            }
        }
    }
}
