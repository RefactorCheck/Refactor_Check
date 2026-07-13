public class rxjava_0293 {

            private boolean d;
            void drain() {
                if (getAndIncrement() != 0) {
                    return;
                }
    
                for (;;) {
                    if (disposed) {
                        queue.clear();
                        return;
                    }
                    if (!active) {
    
                        d = done;
    
                        T t;
    
                        try {
                            t = queue.poll();
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            dispose();
                            queue.clear();
                            downstream.onError(ex);
                            return;
                        }
    
                        boolean empty = t == null;
    
                        if (d && empty) {
                            disposed = true;
                            downstream.onComplete();
                            return;
                        }
    
                        if (!empty) {
                            ObservableSource<? extends U> o;
    
                            try {
                                o = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null ObservableSource");
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                dispose();
                                queue.clear();
                                downstream.onError(ex);
                                return;
                            }
    
                            active = true;
                            o.subscribe(inner);
                        }
                    }
    
                    if (decrementAndGet() == 0) {
                        break;
                    }
                }
            }
}
