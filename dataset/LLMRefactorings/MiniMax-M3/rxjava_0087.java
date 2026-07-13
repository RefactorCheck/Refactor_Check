public class rxjava_0087 {

            void drain() {
                if (getAndIncrement() != 0) {
                    return;
                }
    
                for (;;) {
                    if (isDisposed()) {
                        return;
                    }
    
                    if (!active) {
    
                        boolean d = done;
    
                        CompletableSource cs;
    
                        try {
                            cs = queue.poll();
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            innerError(ex);
                            return;
                        }
    
                        boolean empty = cs == null;
    
                        if (d && empty) {
                            // errors never set done or call drain.
                            downstream.onComplete();
                            return;
                        }
    
                        if (!empty) {
                            subscribeToNext(cs);
                        }
                    }
    
                    if (decrementAndGet() == 0) {
                        break;
                    }
                }
            }

            private void subscribeToNext(CompletableSource cs) {
                active = true;
                cs.subscribe(inner);
                request();
            }
}
