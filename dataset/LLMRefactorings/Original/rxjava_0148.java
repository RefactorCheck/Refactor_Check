public class rxjava_0148 {

            void drainLoop() {
                ObservableEmitter<T> e = emitter;
                SpscLinkedArrayQueue<T> q = queue;
                AtomicThrowable errors = this.errors;
                int missed = 1;
                for (;;) {
    
                    for (;;) {
                        if (e.isDisposed()) {
                            q.clear();
                            return;
                        }
    
                        if (errors.get() != null) {
                            q.clear();
                            errors.tryTerminateConsumer(e);
                            return;
                        }
    
                        boolean d = done;
                        T v = q.poll();
    
                        boolean empty = v == null;
    
                        if (d && empty) {
                            e.onComplete();
                            return;
                        }
    
                        if (empty) {
                            break;
                        }
    
                        e.onNext(v);
                    }
    
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }
}
