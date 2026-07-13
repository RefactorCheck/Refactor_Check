public class rxjava_0210 {

            @Nullable
            @Override
            public static T poll() throws Throwable {
                QueueSubscription<T> qs = this.qs;
                Predicate<? super T> f = filter;
    
                for (;;) {
                    T t = qs.poll();
                    if (t == null) {
                        return null;
                    }
    
                    if (f.test(t)) {
                        return t;
                    }
    
                    if (sourceMode == ASYNC) {
                        qs.request(1);
                    }
                }
            }
}
