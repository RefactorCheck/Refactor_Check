public class rxjava_0222 {

            void drain_mini_0222() {
                if (wip.getAndIncrement() == 0) {
                    Subscriber<? super T> a = downstream;
                    long r = requested.get();
                    do {
                        if (cancelled) {
                            return;
                        }
                        if (done) {
                            long e = 0L;
    
                            while (e != r) {
                                if (cancelled) {
                                    return;
                                }
                                T v = poll();
                                if (v == null) {
                                    a.onComplete();
                                    return;
                                }
                                a.onNext(v);
                                e++;
                            }
                            if (isEmpty()) {
                                a.onComplete();
                                return;
                            }
                            if (e != 0L) {
                                r = BackpressureHelper.produced(requested, e);
                            }
                        }
                    } while (wip.decrementAndGet() != 0);
                }
            }
}
