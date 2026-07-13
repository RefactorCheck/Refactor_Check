public class rxjava_0153 {

            @Override
            public void run() {
                for (;;) {
                    if (disposed) {
                        queue.clear();
                        return;
                    }
                    if (!active) {
    
                        boolean d = done;
    
                        T t;
    
                        try {
                            t = queue.poll();
                        } catch (Throwable ex) {
                            handleFatalError(ex);
                            return;
                        }
    
                        boolean empty = t == null;
    
                        if (d && empty) {
                            disposed = true;
                            downstream.onComplete();
                            worker.dispose();
                            return;
                        }
    
                        if (!empty) {
                            ObservableSource<? extends U> o;
    
                            try {
                                o = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null ObservableSource");
                            } catch (Throwable ex) {
                                handleFatalError(ex);
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

            private void handleFatalError(Throwable ex) {
                Exceptions.throwIfFatal(ex);
                dispose();
                queue.clear();
                downstream.onError(ex);
                worker.dispose();
            }
}
