public class rxjava_0134 {

            @Override
            void drain() {
                if (getAndIncrement() != 0) {
                    return;
                }
    
                AtomicThrowable errors = this.errors;
                ErrorMode errorMode = this.errorMode;
                SimpleQueue<T> queue = this.queue;
    
                do {
                    if (disposed) {
                        queue.clear();
                        return;
                    }
    
                    if (errors.get() != null) {
                        if (errorMode == ErrorMode.IMMEDIATE
                                || (errorMode == ErrorMode.BOUNDARY && !active)) {
                            disposed = true;
                            queue.clear();
                            errors.tryTerminateConsumer(downstream);
                            return;
                        }
                    }
    
                    if (!active) {
    
                        boolean d = done;
                        boolean empty = true;
                        CompletableSource cs = null;
                        try {
                            T v = queue.poll();
                            if (v != null) {
                                cs = Objects.requireNonNull(mapper.apply(v), "The mapper returned a null CompletableSource");
                                empty = false;
                            }
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            disposed = true;
                            queue.clear();
                            upstream.dispose();
                            errors.tryAddThrowableOrReport(ex);
                            errors.tryTerminateConsumer(downstream);
                            return;
                        }
    
                        if (d && empty) {
                            disposed = true;
                            errors.tryTerminateConsumer(downstream);
                            return;
                        }
    
                        if (!empty) {
                            active = true;
                            cs.subscribe(inner);
                        }
                    }
                } while (decrementAndGet() != 0);
            }
}
