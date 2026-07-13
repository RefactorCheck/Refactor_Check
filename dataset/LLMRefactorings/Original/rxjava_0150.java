public class rxjava_0150 {

        boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, AtomicReference<R> q) {
            if (cancelled) {
                q.lazySet(null);
                return true;
            }
    
            if (d) {
                Throwable e = error;
                if (e != null) {
                    q.lazySet(null);
                    a.onError(e);
                    return true;
                } else
                if (empty) {
                    a.onComplete();
                    return true;
                }
            }
    
            return false;
        }
}
