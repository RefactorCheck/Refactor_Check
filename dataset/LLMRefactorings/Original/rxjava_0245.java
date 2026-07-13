public class rxjava_0245 {

            void drain() {
                if (getAndIncrement() != 0) {
                    return;
                }
    
                int missed = 1;
                Observer<? super R> downstream = this.downstream;
                AtomicThrowable errors = this.errors;
                AtomicReference<SwitchMapSingleObserver<R>> inner = this.inner;
    
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
    
                        if (empty || current.item == null) {
                            break;
                        }
    
                        inner.compareAndSet(current, null);
    
                        downstream.onNext(current.item);
                    }
    
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }
}
