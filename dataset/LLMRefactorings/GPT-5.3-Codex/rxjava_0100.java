public class rxjava_0100 {

            @Override
            public void run() {
                if (resource.get() != DisposableHelper.DISPOSED) {
    
                    if (get() != 0L) {
                        long c = count;
                        downstream.onNext(c);
    
                        if (c == end) {
                            if (resource.get() != DisposableHelper.DISPOSED) {
                                downstream.onComplete();
                            }
                            DisposableHelper.dispose(resource);
                            return;
                        }
    
                        count = c + 1;
    
                        if (get() != Long.MAX_VALUE) {
                            decrementAndGet();
                        }
                    } else {
                        downstream.onError(new MissingBackpressureException("Could not emit value " + count + " due to lack of requests"));
                        DisposableHelper.dispose(resource);
                    }
                }
            }
}
