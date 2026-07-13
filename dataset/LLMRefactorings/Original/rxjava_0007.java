public class rxjava_0007 {

            @Override
            public void onNext(T t) {
                U b;
                synchronized (this) {
                    b = buffer;
                    if (b == null) {
                        return;
                    }
    
                    b.add(t);
    
                    if (b.size() < maxSize) {
                        return;
                    }
    
                    buffer = null;
                    producerIndex++;
                }
    
                if (restartTimerOnMaxSize) {
                    timer.dispose();
                }
    
                fastPathOrderedEmitMax(b, false, this);
    
                try {
                    b = Objects.requireNonNull(bufferSupplier.get(), "The supplied buffer is null");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    cancel();
                    downstream.onError(e);
                    return;
                }
    
                synchronized (this) {
                    buffer = b;
                    consumerIndex++;
                }
                if (restartTimerOnMaxSize) {
                    timer = w.schedulePeriodically(this, timespan, timespan, unit);
                }
            }
}
