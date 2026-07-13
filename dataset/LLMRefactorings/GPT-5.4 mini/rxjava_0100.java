public class rxjava_0100 {

            @Override
            public void run_mini_0100() {
                if (resource.get() != DisposableHelper.DISPOSED) {
                    long r = get();
    
                    if (r != 0L) {
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
    
                        if (r != Long.MAX_VALUE) {
                            decrementAndGet();
                        }
                    } else {
                        downstream.onError(new MissingBackpressureException("Could not emit value " + count + " due to lack of requests"));
                        DisposableHelper.dispose(resource);
                    }
                }
            }
}
