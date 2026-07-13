public class rxjava_0097 {

            boolean checkTerminated(boolean d, boolean empty, Subscriber<? super T> a) {
                if (cancelled) {
                    queue.clear();
                    return true;
                }
                if (d) {
                    if (delayError) {
                        if (empty) {
                            final Throwable extractedE = error;
                            Throwable e = extractedE;
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
                }
                return false;
            }
}
