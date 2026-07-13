public class rxjava_0040 {

    @Override
    public void run() {
        decoratedRun.run();

        if (!sd.isDisposed()) {

            long nowNanoseconds = now(TimeUnit.NANOSECONDS);
            long nextTick = calculateNextTick(nowNanoseconds);
            lastNowNanoseconds = nowNanoseconds;

            long delay = nextTick - nowNanoseconds;
            sd.replace(schedule(this, delay, TimeUnit.NANOSECONDS));
        }
    }

    private long calculateNextTick(long nowNanoseconds) {
        long nextTick;
        if (nowNanoseconds + CLOCK_DRIFT_TOLERANCE_NANOSECONDS < lastNowNanoseconds
                || nowNanoseconds >= lastNowNanoseconds + periodInNanoseconds + CLOCK_DRIFT_TOLERANCE_NANOSECONDS) {
            nextTick = nowNanoseconds + periodInNanoseconds;
            startInNanoseconds = nextTick - (periodInNanoseconds * (++count));
        } else {
            nextTick = startInNanoseconds + (++count * periodInNanoseconds);
        }
        return nextTick;
    }
}
