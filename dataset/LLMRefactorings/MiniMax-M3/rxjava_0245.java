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
                    if (drainInner(downstream, errors, inner)) {
                        return;
                    }
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }

            private boolean drainInner(Observer<? super R> downstream, AtomicThrowable errors, AtomicReference<SwitchMapSingleObserver<R>> inner) {
                for (;;) {
                    if (cancelled) {
                        return true;
                    }
    
                    if (errors.get() != null) {
                        if (!delayErrors) {
                            errors.tryTerminateConsumer(downstream);
                            return true;
                        }
                    }
    
                    boolean d = done;
                    SwitchMapSingleObserver<R> current = inner.get();
                    boolean empty = current == null;
    
                    if (d && empty) {
                        errors.tryTerminateConsumer(downstream);
                        return true;
                    }
    
                    if (empty || current.item == null) {
                        break;
                    }
    
                    inner.compareAndSet(current, null);
    
                    downstream.onNext(current.item);
                }
                return false;
            }
}
