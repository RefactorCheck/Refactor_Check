public class rxjava_0206 {

    public static <T> void postComplete(Subscriber<? super T> actual,
                                        Queue<T> queue,
                                        AtomicLong state,
                                        BooleanSupplier isCancelled) {

        if (queue.isEmpty()) {
            actual.onComplete();
            return;
        }

        if (postCompleteDrain(state.get(), actual, queue, state, isCancelled)) {
            return;
        }

        performStateTransition(actual, queue, state, isCancelled);
    }

    private static <T> void performStateTransition(Subscriber<? super T> actual,
                                                    Queue<T> queue,
                                                    AtomicLong state,
                                                    BooleanSupplier isCancelled) {
        for (; ; ) {
            long r = state.get();

            if ((r & COMPLETED_MASK) != 0L) {
                return;
            }

            long u = r | COMPLETED_MASK;
            // (active, r) -> (complete, r) transition
            if (state.compareAndSet(r, u)) {
                // if the requested amount was non-zero, drain the queue
                if (r != 0L) {
                    postCompleteDrain(u, actual, queue, state, isCancelled);
                }

                return;
            }
        }
    }
}
