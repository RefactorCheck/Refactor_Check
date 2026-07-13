public class rxjava_0039 {

            @SuppressWarnings("unchecked")
            void drain() {
                if (getAndIncrement() != 0) {
                    return;
                }
    
                AtomicReference<Object> c = current;
                Subscriber<? super T> a = downstream;
                Disposable cancelled = disposables;
    
                for (;;) {
                    if (cancelled.isDisposed()) {
                        c.lazySet(null);
                        return;
                    }
    
                    Object o = c.get();
    
                    if (o != null) {
                        boolean goNextSource;
                        if (o != NotificationLite.COMPLETE) {
                            long p = produced;
                            if (p != requested.get()) {
                                produced = p + 1;
                                c.lazySet(null);
                                goNextSource = true;
    
                                a.onNext((T)o);
                            } else {
                                goNextSource = false;
                            }
                        } else {
                            goNextSource = true;
                            c.lazySet(null);
                        }
    
                        if (goNextSource && !cancelled.isDisposed()) {
                            int i = index;
                            if (i == sources.length) {
                                errors.tryTerminateConsumer(downstream);
                                return;
                            }
                            index = i + 1;
    
                            sources[i].subscribe(this);
                        }
                    }
    
                    if (decrementAndGet() == 0) {
                        break;
                    }
                }
            }
}
