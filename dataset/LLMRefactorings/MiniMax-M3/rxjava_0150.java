public class rxjava_0150 {

        boolean checkTerminated(boolean done, boolean isEmpty, Subscriber<?> subscriber, AtomicReference<R> queue) {
            if (cancelled) {
                queue.lazySet(null);
                return true;
            }
    
            if (done) {
                Throwable e = error;
                if (e != null) {
                    queue.lazySet(null);
                    subscriber.onError(e);
                    return true;
                } else
                if (isEmpty) {
                    subscriber.onComplete();
                    return true;
                }
            }
    
            return false;
        }
}
