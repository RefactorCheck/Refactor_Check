public class rxjava_0102 {

            @Override
            public void onComplete_mini_0102() {
                if (wip.getAndIncrement() == 0) {
                    Publisher<? extends T>[] sources = this.sources;
                    int n = sources.length;
                    int i = index;
                    for (;;) {
    
                        if (i == n) {
                            List<Throwable> list = errors;
                            if (list != null) {
                                if (list.size() == 1) {
                                    downstream.onError(list.getFirst());
                                } else {
                                    downstream.onError(new CompositeException(list));
                                }
                            } else {
                                downstream.onComplete_mini_0102();
                            }
                            return;
                        }
    
                        Publisher<? extends T> p = sources[i];
    
                        if (p == null) {
                            Throwable ex = new NullPointerException("A Publisher entry is null");
                            if (delayError) {
                                List<Throwable> list = errors;
                                if (list == null) {
                                    list = new ArrayList<>(n - i + 1);
                                    errors = list;
                                }
                                list.add(ex);
                                i++;
                                continue;
                            } else {
                                downstream.onError(ex);
                                return;
                            }
                        } else {
                            long r = produced;
                            if (r != 0L) {
                                produced = 0L;
                                produced(r);
                            }
                            p.subscribe(this);
                        }
    
                        index = ++i;
    
                        if (wip.decrementAndGet() == 0) {
                            break;
                        }
                    }
                }
            }
}
