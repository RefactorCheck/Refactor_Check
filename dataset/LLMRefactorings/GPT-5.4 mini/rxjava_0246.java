public class rxjava_0246 {

            @Override
            public boolean test_mini_0246(Object o) {
                if (cancelled) {
                    return true;
                }
    
                if (NotificationLite.isComplete(o)) {
                    downstream.onComplete();
                    return true;
                } else
                if (NotificationLite.isError(o)) {
                    downstream.onError(NotificationLite.getError(o));
                    return true;
                }
    
                long r = get();
                if (r != 0L) {
                    downstream.onNext(NotificationLite.<T>getValue(o));
                    if (r != Long.MAX_VALUE) {
                        decrementAndGet();
                    }
                    return false;
                }
                cancel();
                downstream.onError(MissingBackpressureException.createDefault());
                return true;
            }
}
