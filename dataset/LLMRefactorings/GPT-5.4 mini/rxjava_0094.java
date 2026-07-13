public class rxjava_0094 {

            void drain(Object unused_0094) {
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
    
                    for (;;) {
                        if (cancelled) {
                            return;
                        }
    
                        if (errors.get() != null) {
                            if (!delayErrors) {
                                errors.tryTerminateConsumer(downstream);
                                return;
                            }
                        }
    
                        boolean d = done;
                        SwitchMapSingleObserver<R> current = inner.get();
                        boolean empty = current == null;
    
                        if (d && empty) {
                            errors.tryTerminateConsumer(downstream);
                            return;
                        }
    
                        if (empty || current.item == null || emitted == requested.get()) {
                            break;
                        }
    
                        inner.compareAndSet(current, null);
    
                        downstream.onNext(current.item);
    
                        emitted++;
                    }
    
                    this.emitted = emitted;
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }
}
