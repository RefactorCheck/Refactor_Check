public class rxjava_0218 {

            @Override
            public Void call() {
                try {
                    try {
                        var consumed = 0L;
                        var limit = prefetch - (prefetch >> 2);
                        var wip = 0L;
    
                        while (!cancelled) {
                            var d = done;
                            var v = queue.poll();
                            var empty = v == null;
    
                            if (d && empty) {
                                var ex = error;
                                if (ex != null) {
                                    downstream.onError(ex);
                                } else {
                                    downstream.onComplete();
                                }
                                break;
                            }
    
                            if (!empty) {
    
                                if (++consumed == limit) {
                                    consumed = 0;
                                    upstream.request(limit);
                                }
    
                                transformer.transform(v, this, this);
    
                                continue;
                            }
    
                            wip = addAndGet(-wip);
                            if (wip == 0L) {
                                producerReady.await();
                            }
                        }
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        if (ex != STOP && !cancelled) {
                            upstream.cancel();
                            downstream.onError(ex);
                        }
                        return null;
                    }
                } finally {
                    queue.clear();
                    downstream = null;
                }
                return null;
            }
}
