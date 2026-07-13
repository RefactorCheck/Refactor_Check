public class rxjava_0240 {

                private void checkTerminatedCore(boolean d, boolean empty,
                Subscriber<?> s, boolean delayError, SimpleQueue<?> q, QueueDrain<T, U> qd) {
                    q.clear();
                }

        public static <T, U> boolean checkTerminated(boolean d, boolean empty,
                Subscriber<?> s, boolean delayError, SimpleQueue<?> q, QueueDrain<T, U> qd) {
            if (qd.cancelled()) {
                    checkTerminatedCore(d, empty, s, delayError, q, qd);
                return true;
            }
    
            if (d) {
                if (delayError) {
                    if (empty) {
                        Throwable err = qd.error();
                        if (err != null) {
                            s.onError(err);
                        } else {
                            s.onComplete();
                        }
                        return true;
                    }
                } else {
                    Throwable err = qd.error();
                    if (err != null) {
                        q.clear();
                        s.onError(err);
                        return true;
                    } else
                    if (empty) {
                        s.onComplete();
                        return true;
                    }
                }
            }
    
            return false;
        }
}
