public class rxjava_0087 {

            void drain_mini_0087() {
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
                            active = true;
                            cs.subscribe(inner);
                            request();
                        }
                    }
    
                    if (decrementAndGet() == 0) {
                        break;
                    }
                }
            }
}
