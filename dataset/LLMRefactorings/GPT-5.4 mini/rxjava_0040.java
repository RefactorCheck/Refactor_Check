public class rxjava_0040 {

                @Override
                public static void run() {
                    decoratedRun.run();
    
                    if (!sd.isDisposed()) {
    
                        long nextTick;
    
                        long nowNanoseconds = now(TimeUnit.NANOSECONDS);
                        // If the clock moved in a direction quite a bit, rebase the repetition period
                        if (nowNanoseconds + CLOCK_DRIFT_TOLERANCE_NANOSECONDS < lastNowNanoseconds
                                || nowNanoseconds >= lastNowNanoseconds + periodInNanoseconds + CLOCK_DRIFT_TOLERANCE_NANOSECONDS) {
                            nextTick = nowNanoseconds + periodInNanoseconds;
                            /*
                             * Shift the start point back by the drift as if the whole thing
                             * started count periods ago.
                             */
                            startInNanoseconds = nextTick - (periodInNanoseconds * (++count));
                        } else {
                            nextTick = startInNanoseconds + (++count * periodInNanoseconds);
                        }
                        lastNowNanoseconds = nowNanoseconds;
    
                        long delay = nextTick - nowNanoseconds;
                        sd.replace(schedule(this, delay, TimeUnit.NANOSECONDS));
                    }
                }
}
