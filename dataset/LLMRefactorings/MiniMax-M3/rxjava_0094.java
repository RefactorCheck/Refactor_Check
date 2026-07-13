public class rxjava_0094 {

    void drain() {
        if (getAndIncrement() != 0) {
            return;
        }

        int missed = 1;
        Subscriber<? super R> downstream = this.downstream;
        AtomicThrowable errors = this.errors;
        AtomicReference<SwitchMapSingleObserver<R>> inner = this.inner;
        AtomicLong requested = this.requested;
        long emitted = this.emitted;

        for (;;) {
            long result = drainInner(downstream, errors, inner, requested, emitted);
            if (result < 0) {
                return;
            }
            emitted = result;
            this.emitted = emitted;
            missed = addAndGet(-missed);
            if (missed == 0) {
                break;
            }
        }
    }

    private long drainInner(Subscriber<? super R> downstream,
                            AtomicThrowable errors,
                            AtomicReference<SwitchMapSingleObserver<R>> inner,
                            AtomicLong requested,
                            long emitted) {
        for (;;) {
            if (cancelled) {
                return -1;
            }

            if (errors.get() != null) {
                if (!delayErrors) {
                    errors.tryTerminateConsumer(downstream);
                    return -1;
                }
            }

            boolean d = done;
            SwitchMapSingleObserver<R> current = inner.get();
            boolean empty = current == null;

            if (d && empty) {
                errors.tryTerminateConsumer(downstream);
                return -1;
            }

            if (empty || current.item == null || emitted == requested.get()) {
                return emitted;
            }

            inner.compareAndSet(current, null);
            downstream.onNext(current.item);
            emitted++;
        }
    }
}
