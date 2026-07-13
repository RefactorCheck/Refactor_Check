public class rxjava_0238 {

            boolean checkTerminated(boolean empty, Subscriber<? super T> a, boolean delayError) {
                if (cancelled) {
                    queue.clear();
                    return true;
                }
                if (delayError) {
                    if (empty) {
                        Throwable e = error;
                        if (e != null) {
                            a.onError(e);
                        } else {
                            a.onComplete();
                        }
                        return true;
                    }
                } else {
                    Throwable e = error;
                    if (e != null) {
                        queue.clear();
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
