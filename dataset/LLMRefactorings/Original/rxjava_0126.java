public class rxjava_0126 {

            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
    
                if (!gate) {
                    gate = true;
                    long r = get();
                    if (r != 0L) {
                        downstream.onNext(t);
                        BackpressureHelper.produced(this, 1);
                    } else {
                        upstream.cancel();
                        done = true;
                        downstream.onError(MissingBackpressureException.createDefault());
                        worker.dispose();
                        return;
                    }
    
                    Disposable d = timer.get();
                    if (d != null) {
                        d.dispose();
                    }
    
                    timer.replace(worker.schedule(this, timeout, unit));
                } else if (onDropped != null) {
                    try {
                        onDropped.accept(t);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        upstream.cancel();
                        done = true;
                        downstream.onError(ex);
                        worker.dispose();
                    }
                }
            }
}
