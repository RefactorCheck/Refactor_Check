public class rxjava_0240 {

    public static <T, U> boolean checkTerminated(boolean d, boolean empty,
            Subscriber<?> s, boolean delayError, SimpleQueue<?> q, QueueDrain<T, U> qd) {
        if (qd.cancelled()) {
            q.clear();
            return true;
        }

        if (d) {
            if (delayError) {
                if (empty) {
                    Throwable error = qd.error();
                    if (error != null) {
                        s.onError(error);
                    } else {
                        s.onComplete();
                    }
                    return true;
                }
            } else {
                Throwable error = qd.error();
                if (error != null) {
                    q.clear();
                    s.onError(error);
                    return true;
                } else if (empty) {
                    s.onComplete();
                    return true;
                }
            }
        }

        return false;
    }
}
