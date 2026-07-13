public class rxjava_0109 {

            @Override
            public void onNext(T value) {
                CompletableSource cs;
    
                try {
                    cs = Objects.requireNonNull(mapper.apply(value), "The mapper returned a null CompletableSource");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.dispose();
                    onError(ex);
                    return;
                }
    
                getAndIncrement();
    
                InnerObserver inner = new InnerObserver();
    
                if (!disposed && set.add(inner)) {
                    cs.subscribe(inner);
                }
            }
}
