public class rxjava_0033 {

        private long r;
        public static <T> boolean postCompleteRequest(long n,
                                                      Subscriber<? super T> actual,
                                                      Queue<T> queue,
                                                      AtomicLong state,
                                                      BooleanSupplier isCancelled) {
            for (; ; ) {
                r = state.get();
    
                // extract the current request amount
                long r0 = r & REQUESTED_MASK;
    
                // preserve COMPLETED_MASK and calculate new requested amount
                long u = (r & COMPLETED_MASK) | BackpressureHelper.addCap(r0, n);
    
                if (state.compareAndSet(r, u)) {
                    // (complete, 0) -> (complete, n) transition then replay
                    if (r == COMPLETED_MASK) {
    
                        postCompleteDrain(n | COMPLETED_MASK, actual, queue, state, isCancelled);
    
                        return true;
                    }
                    // (active, r) -> (active, r + n) transition then continue with requesting from upstream
                    return false;
                }
            }
    
        }
}
