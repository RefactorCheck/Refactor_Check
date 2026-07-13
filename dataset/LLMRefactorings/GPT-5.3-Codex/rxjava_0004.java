public class rxjava_0004 {

            @Override
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public void onNext(T t) {
                SwitchMapSingleObserver<R> current = inner.get();
                if (current != null) {
                    current.dispose();
                }
    
                SingleSource<? extends R> ss;
    
                try {
                    ss = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null SingleSource");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.cancel();
                    inner.getAndSet((SwitchMapSingleObserver)INNER_DISPOSED);
                    onError(ex);
                    return;
                }
    
                SwitchMapSingleObserver<R> observer = new SwitchMapSingleObserver<>(this);
    
                for (;;) {
                    current = inner.get();
                    if (current == INNER_DISPOSED) {
                        break;
                    }
                    if (inner.compareAndSet(current, observer)) {
                        ss.subscribe(observer);
                        break;
                    }
                }
            }
}
