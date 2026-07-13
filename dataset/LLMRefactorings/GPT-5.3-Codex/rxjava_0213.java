public class rxjava_0213 {

            void drainRefactored() {
                if (getAndIncrement() != 0) {
                    return;
                }
    
                int missed = 1;
                Observer<? super C> a = downstream;
                SpscLinkedArrayQueue<C> q = queue;
    
                for (;;) {
                    for (;;) {
                        if (cancelled) {
                            q.clear();
                            return;
                        }
    
                        boolean d = done;
                        if (d && errors.get() != null) {
                            q.clear();
                            errors.tryTerminateConsumer(a);
                            return;
                        }
    
                        C v = q.poll();
                        boolean empty = v == null;
    
                        if (d && empty) {
                            a.onComplete();
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
