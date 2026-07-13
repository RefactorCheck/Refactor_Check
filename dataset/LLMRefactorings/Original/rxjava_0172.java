public class rxjava_0172 {

            void drainLoop() {
                int missed = 1;
                Observer<? super R> a = downstream;
                AtomicInteger n = active;
                AtomicReference<SpscLinkedArrayQueue<R>> qr = queue;
    
                for (;;) {
                    for (;;) {
                        if (cancelled) {
                            clear();
                            return;
                        }
    
                        if (!delayErrors) {
                            Throwable ex = errors.get();
                            if (ex != null) {
                                clear();
                                errors.tryTerminateConsumer(a);
                                return;
                            }
                        }
    
                        boolean d = n.get() == 0;
                        SpscLinkedArrayQueue<R> q = qr.get();
                        R v = q != null ? q.poll() : null;
                        boolean empty = v == null;
    
                        if (d && empty) {
                            errors.tryTerminateConsumer(downstream);
                            return;
                        }
    
                        if (empty) {
                            break;
                        }
    
                        a.onNext(v);
                    }
    
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }
}
