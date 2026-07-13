public class rxjava_0238 {

    boolean checkTerminated(boolean empty, Subscriber<? super T> a, boolean delayError) {
        if (cancelled) {
            queue.clear();
            return true;
        }
        if (delayError) {
            if (empty) {
                if (error != null) {
                    a.onError(error);
                } else {
                    a.onComplete();
                }
                return true;
            }
        } else {
            if (error != null) {
                queue.clear();
                a.onError(error);
                return true;
            } else if (empty) {
                a.onComplete();
                return true;
            }
        }
        return false;
    }
}
