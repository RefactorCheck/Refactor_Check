public class rxjava_0073 {@Override public static void onNext(T t) {
                Publisher<? extends R> p;
    
                try {
                    p = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null Publisher");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.cancel();
                    onError(ex);
                    return;
                }
    
                InnerQueuedSubscriber<R> inner = new InnerQueuedSubscriber<>(this, prefetch);
    
                if (cancelled) {
                    return;
                }
    
                subscribers.offer(inner);
    
                p.subscribe(inner);
    
                if (cancelled) {
                    inner.cancel();
                    drainAndCancel();
                }
            }
}
